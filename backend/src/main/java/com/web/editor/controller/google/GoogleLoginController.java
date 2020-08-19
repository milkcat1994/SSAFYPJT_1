package com.web.editor.controller.google;


import com.web.editor.model.dto.user.User;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.google.GoogleUserService;
import com.web.editor.model.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/auth/google")
public class GoogleLoginController {

    @Autowired
    GoogleUserService googleUserService;
    @Autowired
    UserService userService;

    // 구글계정 로그인
	@ApiOperation(value = "구글 로그인/PlatformType: success, kakao, no connect ")
	@PostMapping("/{email}")
	public Object googleLogin(@PathVariable String email) {
        BasicResponse result = new BasicResponse();
        User user = googleUserService.googleLogin(email);
        // 회원 정보가 없음.
        if (user == null) {
            result.status = true;
            result.data = "not exist";
            result.object = null;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        // 회원정보 있음
        String platformType = user.getPlatformType();
        if (platformType.equals("google")){
            // 구글 연동되어 있음
            result.status = true;
            result.data = "success";
            result.object = user;
        } else if (platformType.equals("kakao")) {
            // 카카오에 연동되어있음
            result.status = true;
            result.data = "kakao";
            result.object = user;
        } else {
            // 연동되어있지 않음
            result.status = true;
            result.data = "normal";
            result.object = user;
        }
		
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


	@ApiOperation(value = "구글 연동")
	@PostMapping("/connect/{uid}")
	public Object googleConnect(@PathVariable int uid) {
        int res = googleUserService.googleConnect(uid);
        BasicResponse result = new BasicResponse();
        if (res > 0) {
            result.data = "success";
            result.status = true;
            result.object = userService.findByUid(uid + "");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>("fail", HttpStatus.OK);
    }
}