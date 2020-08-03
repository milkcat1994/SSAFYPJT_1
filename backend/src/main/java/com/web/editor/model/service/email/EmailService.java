package com.web.editor.model.service.email;

import com.web.editor.model.dto.email.EmailCheckRequest;
import com.web.editor.model.dto.email.EmailConfirm;

public interface EmailService {
    
    // email이용한 등록여부 확인
    EmailConfirm findEmailConfirmByEmail(String email);

    // email과 key를 이용한 등록 여부 확인
    EmailConfirm findEmailConfirmByEmailAndEmailKey(EmailCheckRequest emailCheckRequest);

    //email 인증 코드 등록
    public int createEmailCode(EmailConfirm emailConfirm);

    // email 인증 코드 수정
    int updateEmail(EmailConfirm emailConfirm);

    // email 인증 코드 삭제
    int deleteEmailConfirmByEmail(String email);
}