package com.web.editor.model.service.email;

import java.sql.SQLException;

import com.web.editor.model.dto.email.EmailCheckRequest;
import com.web.editor.model.dto.email.EmailConfirm;
import com.web.editor.model.mapper.email.EmailMapper;

import org.springframework.beans.factory.annotation.Autowired;

public class EmailServiceImpl implements EmailService {

    @Autowired
    EmailMapper emailMapper;

    @Override
    public EmailConfirm findEmailConfirmByEmail(String email) {
        try{
            EmailConfirm emailConfirm = emailMapper.findEmailConfirmByEmail(email);
            return emailConfirm;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EmailConfirm findEmailConfirmByEmailAndEmailKey(EmailCheckRequest emailCheckRequest) {
        try{
            EmailConfirm emailConfirm = emailMapper.findEmailConfirmByEmailAndEmailKey(emailCheckRequest);
            return emailConfirm;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int createEmailCode(EmailConfirm emailConfirm) {
        try{
            int res = emailMapper.createEmailCode(emailConfirm);
            return res;
        } catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int updateEmail(EmailConfirm emailConfirm) {
        try{
            int res = emailMapper.updateEmail(emailConfirm);
            return res;
        } catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteEmailConfirmByEmail(String email) {
        try{
            int res = emailMapper.deleteEmailConfirmByEmail(email);
            return res;
        } catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    
}