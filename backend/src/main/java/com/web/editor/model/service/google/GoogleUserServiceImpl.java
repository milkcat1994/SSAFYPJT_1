package com.web.editor.model.service.google;

import java.sql.SQLException;

import com.web.editor.model.dto.user.User;
import com.web.editor.model.mapper.user.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleUserServiceImpl implements GoogleUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User googleLogin(String email) {
        try{
            return userMapper.googleLogin(email);
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int googleConnect(int uid) {
        int res;
        try {
            res = userMapper.googleConnect(uid);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    
}