package com.web.editor.model.service.user;

import com.web.editor.model.dto.user.NormalLoginRequest;
import com.web.editor.model.dto.user.User;
import com.web.editor.model.dto.user.UserConfirm;

public interface UserService {
    
    // 일반 로그인
    public User normalLogin(NormalLoginRequest normalLoginRequest);

    // 일반 회원가입
    public int normalRegister(User user);

    // 회원 조회
    public User findByUid(String uid);

    // 닉네임으로 회원 조회
    public String userfindByNickname(String nickname);

    // 회원 정보 수정
    public int updateUser(User user);

    // 회원 탈퇴
    public int deleteUserByUid(String uid);


    // 회원가입 시 있는 유저인지 파악
    // email, platform_type을 이용해 찾기
    public User findByEmailAndPlatformType(UserConfirm userConfirm);

    // nickname 중복 확인
    public User findByNickname(String nickname);
}