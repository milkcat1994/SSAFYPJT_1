package com.web.editor.util.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

// 참고 : https://m.blog.naver.com/PostView.nhn?blogId=hyoun1202&logNo=220732513841&proxyReferer=https:%2F%2Fwww.google.com%2F
@Component
public class SecurityUtil {
    public String encryptSHA256(String str){
        String sha = "";
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            //첫 입력은 salt값
            sh.update("1fhdhkwjs".getBytes());
            // 두번째 입력은 사용자의 패스워드
            sh.update(str.getBytes());
            sha = String.format("%064x", new BigInteger(1, sh.digest()));
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            System.out.println("Encrypt Error - NoSuchAlgorithmException");
            sha = null;
        }
        // 암호화 된 비밀번호 추출
        // System.out.println(sha);
        return sha;
    }
}