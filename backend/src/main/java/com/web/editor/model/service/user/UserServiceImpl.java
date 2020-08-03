package com.web.editor.model.service.user;

import java.sql.SQLException;

import com.web.editor.model.dto.user.NormalLoginRequest;
import com.web.editor.model.dto.user.User;
import com.web.editor.model.dto.user.UserConfirm;
import com.web.editor.model.mapper.user.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User normalLogin(NormalLoginRequest normalLoginRequest) {
        User user = null;
        try{
            user = userMapper.normalLogin(normalLoginRequest);
            return user;
        } catch(SQLException e){
            e.printStackTrace();
            return user;
        }
    }

    @Override
    public int normalRegister(User user) {
        try{
            int res = userMapper.normalRegister(user);
            return res;
        } catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public User findByUid(String uid) {
        User user = null;
        try{
            user = userMapper.findByUid(uid);
            return user;
        } catch(SQLException e){
            e.printStackTrace();
            return user;
        }
    }

    @Override
    public int updateUser(User user) {
        try{
            int res = userMapper.updateUser(user);
            return res;
        } catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteUserByUid(String uid) {
        try{
            int res = userMapper.deleteUserByUid(uid);
            return res;
        } catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public User findByEmailAndPlatformType(UserConfirm userConfirm) {
        User user = null;
        try{
            user = userMapper.findByEmailAndPlatformType(userConfirm);
            return user;
        } catch(SQLException e){
            e.printStackTrace();
            return user;
        }
    }

    @Override
    public User findByNickname(String nickname) {
        User user = null;
        try{
            user = userMapper.findByNickname(nickname);
            return user;
        } catch(SQLException e){
            e.printStackTrace();
            return user;
        }
    }
    
}