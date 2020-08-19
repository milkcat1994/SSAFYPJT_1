package com.web.editor.controller.email;

import java.sql.SQLException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.web.editor.model.dto.email.EmailCheckRequest;
import com.web.editor.model.dto.email.EmailConfirm;
import com.web.editor.model.dto.email.EmailRequest;
import com.web.editor.model.dto.user.User;
import com.web.editor.model.dto.user.UserConfirm;
import com.web.editor.model.mapper.email.EmailMapper;
import com.web.editor.model.mapper.user.UserMapper;
import com.web.editor.model.response.BasicResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/token")
    @ApiOperation(value = "이메일 토큰 발송")
    public Object sendEmailAction(@RequestBody EmailRequest request) {
        String nickname = request.getNickname();
        String email = request.getEmail();
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        User user;
        try {
            user = userMapper.findByEmailAndPlatformType(new UserConfirm(email, "normal"));
            // 이미 해당 이메일이 존재한다면
            if (user != null) {
                result.status = false;
                result.data = "duplicate";
                response = new ResponseEntity<>(result, HttpStatus.OK);
                return response;
            }
            try {
                // 랜덤 코드 만들기
                int randomCode = new Random().nextInt(10000) + 1000;
                String joinCode = String.valueOf(randomCode);

                EmailConfirm emailConfirm = emailMapper.findEmailConfirmByEmail(email);
                // 이미 이메일이 있다면 update
                if (emailConfirm != null) {
                    emailMapper.updateEmail(new EmailConfirm(email, joinCode));
                } else {
                    emailMapper.createEmailCode(new EmailConfirm(email, joinCode));
                }

                MimeMessage msg = mailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(msg, true, "UTF-8");

                messageHelper.setSubject(nickname + "님 회원가입 인증 메일입니다.");
                messageHelper.setText("인증번호는 " + joinCode + " 입니다.");
                messageHelper.setTo(email);
                msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email));
                mailSender.send(msg);
                result.status = true;
                result.data = "success";
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } catch (SQLException se) {
                se.printStackTrace();
                result.status = false;
                result.data = "sql_error";
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } catch (MessagingException me) {
                me.printStackTrace();
                result.status = false;
                result.data = "message_error";
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            result.status = false;
            result.data = "sql_error";
        }

        return response;
    }

    @PostMapping("/token/check")
    @ApiOperation(value = "이메일 토큰 확인")
    public Object tokenCheck(@RequestBody EmailCheckRequest request) {
        EmailConfirm emailConfirm;

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        
        try {
            emailConfirm = emailMapper.findEmailConfirmByEmailAndEmailKey(request);
        } catch (SQLException se) {
            se.printStackTrace();
            result.status = false;
            result.data = "sql_error";
            response = new ResponseEntity<>(result, HttpStatus.OK);
            return response;
        }
        // 이메일 인증 코드 일치
        if (emailConfirm != null) {
            result.status = true;
            result.data = "success";
            // 기존 존재하는 토큰 삭제
            try {
                emailMapper.deleteEmailConfirmByEmail(request.getEmail());
            } catch (SQLException se) {
                se.printStackTrace();
                result.status = false;
                result.data = "sql_error";
                response = new ResponseEntity<>(result, HttpStatus.OK);
                return response;
            }
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }
}