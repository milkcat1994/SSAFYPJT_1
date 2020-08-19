package com.web.editor.model.service.user;

import java.sql.SQLException;

import com.web.editor.model.dto.user.NormalLoginRequest;
import com.web.editor.model.dto.user.User;
import com.web.editor.model.dto.user.UserConfirm;
import com.web.editor.model.mapper.user.UserMapper;
import com.web.editor.util.security.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    SecurityUtil securityUtil;

    @Override
    public User normalLogin(NormalLoginRequest normalLoginRequest) {
        User user = null;
        try{
            //sha-256 암호화하여 DB와 비교
            normalLoginRequest.setPassword(securityUtil.encryptSHA256(normalLoginRequest.getPassword()));
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
            user.setPassword(securityUtil.encryptSHA256(user.getPassword()));
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
    public String userfindByNickname(String nickname) {
        try{
            return userMapper.userfindByNickname(nickname);
        } catch(SQLException e){
            e.printStackTrace();
            return null;
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