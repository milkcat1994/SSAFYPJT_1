package com.web.editor.model.mapper.user;

import java.sql.SQLException;

import com.web.editor.model.dto.user.NormalLoginRequest;
import com.web.editor.model.dto.user.User;
import com.web.editor.model.dto.user.UserConfirm;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class UserMapper {
    
    @Autowired
    @Qualifier("firstSqlSessionTemplate")
    private SqlSession sqlSession;

    // 일반 로그인
    public User normalLogin(NormalLoginRequest normalLoginRequest) throws SQLException{
        return sqlSession.selectOne("normalLogin", normalLoginRequest);
    }
    // 구글 로그인
    public User googleLogin(String email) throws SQLException{
        return sqlSession.selectOne("googleLogin", email);
    }
    // 구글 연동
    public int googleConnect(int uid) throws SQLException{
        return sqlSession.update("googleConnect", uid);
    }

    // 일반 회원가입
    public int normalRegister(User user) throws SQLException{
        return sqlSession.insert("normalRegister", user);
    }

    // 회원 조회
    public User findByUid(@Param("uid") String uid) throws SQLException{
        return sqlSession.selectOne("findByUid", uid);
    }

    // 회원 조회 닉네임으로
    public String userfindByNickname(@Param("nickname") String nickname) throws SQLException{
        return sqlSession.selectOne("userfindByNickname", nickname);
    }

    // 회원 정보 수정
    public int updateUser(User user) throws SQLException{
        return sqlSession.update("updateUser", user);
    }

    // 회원 탈퇴
    public int deleteUserByUid(@Param("uid") String uid) throws SQLException{
        return sqlSession.delete("deleteUserByUid", uid);
    }

    // 회원가입 시 있는 유저인지 파악
    // email, platform_type을 이용해 찾기
    public User findByEmailAndPlatformType(UserConfirm userConfirm) throws SQLException{
        return sqlSession.selectOne("findByEmailAndPlatformType", userConfirm);
    }

    // nickname 중복 확인
    public User findByNickname(@Param("nickname") String nickname) throws SQLException{
        return sqlSession.selectOne("findByNickname", nickname);
    }
}
