package com.web.editor.controller.user;

import javax.validation.Valid;

import com.web.editor.model.dto.request.NicknameUpdateDto;
import com.web.editor.model.dto.user.NormalLoginRequest;
import com.web.editor.model.dto.user.NormalRegisterRequest;
import com.web.editor.model.dto.user.PortfolioNicknameUpdateRequest;
import com.web.editor.model.dto.user.User;
import com.web.editor.model.dto.user.UserConfirm;
import com.web.editor.model.dto.user.UserUpdateRequest;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.request.RequestService;
import com.web.editor.model.service.user.PortfolioService;
import com.web.editor.model.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    PortfolioService portfolioService;

    @Autowired
    RequestService requestService;

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public Object login(@Valid @RequestBody NormalLoginRequest request) {
        User user = userService.normalLogin(request);

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        if (user != null) {
            result.status = true;
            result.data = "success";
            result.object = user;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("")
    @ApiOperation(value = "회원가입")
    public Object signup(@Valid @RequestBody NormalRegisterRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();
        String auth = request.getAuth();
        // 이미 있는 User인지 확인
        // email중복
        final BasicResponse result = new BasicResponse();
        String message = "";
        User user = userService.findByEmailAndPlatformType(new UserConfirm(email, "normal"));
        if (user == null) {
            user = userService.findByNickname(nickname);
            if (user != null) {
                message = "nickname";
            }
        } else {
            message = "email";
        }
        // 중복 있을 경우 회원가입 실패
        if (user != null) {
            result.status = false;
            result.data = message;
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // 중복 없을경우 회원가입 진행
            user = new User(email, password, nickname, "normal", auth);
            int res = userService.normalRegister(user);
            // editor일 때만 포트폴리오 등록
            if (auth.equals("editor"))
                portfolioService.portfolioInitSave(String.valueOf(userService.findByNickname(nickname).getUid()));
            if (res == -1) {
                result.status = false;
                result.data = "fail";
            } else {
                result.status = true;
                result.data = "success";
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping("/nickname/check")
    @ApiOperation(value = "nickname 중복체크")
    public Object nicknamecheck(@RequestBody NormalRegisterRequest request) {

        final BasicResponse result = new BasicResponse();
        User user = userService.findByNickname(request.getNickname());
        if (user == null) {
            result.status = true;
            result.data = "success";
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.status = true;
        result.data = "false";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/user/{uid}")
    @ApiOperation(value = "user정보 반환")
    public Object findByUid(@PathVariable String uid) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        User user = userService.findByUid(uid);

        if (user != null) {
            result.status = true;
            result.data = "success";
            result.object = new User(user.getEmail(), user.getNickname());
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("/userfind/{nickname}")
    @ApiOperation(value = "user정보 반환-nickname으로 검색")
    public Object userfindByNickname(@PathVariable String nickname) {

        String email = userService.userfindByNickname(nickname);

        if (email != null) {
            return new ResponseEntity<>(email, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("not exist", HttpStatus.OK);
        }
    }

    @PutMapping("/user/{uid}")
    @ApiOperation(value = "user정보 수정")
    public Object updateUser(@RequestBody UserUpdateRequest request, @PathVariable String uid) {
        ResponseEntity response = null;
        String nickname = request.getNickname();
        User user = new User();
        User orgUser = new User();

        final BasicResponse result = new BasicResponse();
        // uid가 int로 변하지 못합니다.
        String message = "uid_need_int";
        try {
            user.setUid(Integer.parseInt(uid));
            // 포트폴리오에 해당 uid있다면 nickname수정할 것.

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            result.status = false;
            result.data = message;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        // 유저의 이전 정보 가져오기
        orgUser = userService.findByUid(uid);

        user.setNickname(nickname);
        int res = userService.updateUser(user);
        portfolioService.portfolioNicknameUpdate(
                new PortfolioNicknameUpdateRequest(Integer.parseInt(uid), request.getNickname()));

        if (res > 0) {
            result.status = true;
            result.data = "success";

            NicknameUpdateDto nicknameUpdateDto = new NicknameUpdateDto();
            // request notify 의 이전 닉네임
            nicknameUpdateDto.setOrgNickname(orgUser.getNickname());
            // 유저의 타입(편집자, 일반회원)
            String auth = orgUser.getAuth();
            // 새로운 닉네임
            nicknameUpdateDto.setNickname(nickname);
            
            // 요청서, 리뷰의 닉네임 변경
            if (auth.equals("editor")){
                requestService.updateNicknameRes(nicknameUpdateDto);
                requestService.updateNickReview(nicknameUpdateDto);
            }else if (auth.equals("noneditor")){
                requestService.updateNicknameReq(nicknameUpdateDto);
            }
            // 알람 닉네임 변경
            requestService.updateReqNoti(nicknameUpdateDto);
            requestService.updateResNoti(nicknameUpdateDto);

            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(value = "회원 탈퇴")
    public Object deleteUserByUid(@PathVariable String id) {
        String uid = id;
        int res = userService.deleteUserByUid(uid);
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        if (res > 0) {
            result.status = true;
            result.data = "success";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }
}