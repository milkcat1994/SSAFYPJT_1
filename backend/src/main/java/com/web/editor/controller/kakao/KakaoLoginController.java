package com.web.editor.controller.kakao;
 
import java.util.HashMap;

import javax.servlet.http.HttpSession;
import com.web.editor.model.dto.user.User;
import com.web.editor.model.dto.user.UserConfirm;
import com.web.editor.model.response.BasicResponse;
import org.springframework.http.ResponseEntity;
import com.web.editor.model.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import com.web.editor.model.dto.user.NormalRegisterRequest;
import io.swagger.annotations.ApiOperation;
import com.web.editor.model.service.kakao.KakaoAPI;
 
// @Controller
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/kakao")
public class KakaoLoginController {
 
    @Autowired
    private KakaoAPI kakao;
    @Autowired
    UserService userService;
	
    @RequestMapping(value="/")
    public String index() {
        return "index";
    }
   
    @PostMapping(value="/login")
    public Object login(@RequestParam("code") String code, HttpSession session,@Valid @RequestBody NormalRegisterRequest request) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        String access_Token = kakao.getAccessToken(code);
        HashMap<String, Object> userInfo = kakao.getUserInfo(access_Token);
        String email = (String) userInfo.get("email");
        User user = userService.findByEmailAndPlatformType(new UserConfirm(email, "kakao"));
        
        if(user != null){ //이미 가입이 되어 있으면 로그인 된걸로
            result.status = true;
            result.data = "success";
            result.object = new User();
            ((User) result.object).setUid(user.getUid());
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else { //가입이 안되어 있으면 회원가입 절차로
            if (userInfo.get("email") != null) {
                session.setAttribute("userId", userInfo.get("email"));
                session.setAttribute("access_Token", access_Token);
                }
            //user = new User(email, null, nickname, "kakao", auth);
        }

        return response;
    }
    
    
    @PostMapping(value="/logout")
    @ApiOperation(value = "로그아웃")
    public Object logout(HttpSession session) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        kakao.kakaoLogout((String)session.getAttribute("access_Token"));
        session.removeAttribute("access_Token");
        session.removeAttribute("userId");
        response = new ResponseEntity<>(result, HttpStatus.OK);
        
        return response;
    }
    
}
