package com.web.editor.model.mapper.email;

import java.sql.SQLException;

import com.web.editor.model.dto.email.EmailCheckRequest;
import com.web.editor.model.dto.email.EmailConfirm;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class EmailMapper {

    @Autowired
    @Qualifier("ThirdSqlSessionTemplate")
    // @Qualifier("SecondSqlSessionTemplate")
    private SqlSession sqlSession;

    // email이용한 등록여부 확인
    public EmailConfirm findEmailConfirmByEmail(String email) throws SQLException{
        return sqlSession.selectOne("findEmailConfirmByEmail", email);
    }

    // email과 key를 이용한 등록 여부 확인
    public EmailConfirm findEmailConfirmByEmailAndEmailKey(EmailCheckRequest emailCheckRequest) throws SQLException{
        return sqlSession.selectOne("findEmailConfirmByEmailAndEmailKey", emailCheckRequest);
    }

    //email 인증 코드 등록
    public int createEmailCode(EmailConfirm emailConfirm) throws SQLException{
        return sqlSession.insert("createEmailCode", emailConfirm);
    }

    // email 인증 코드 수정
    public int updateEmail(EmailConfirm emailConfirm) throws SQLException{
        return sqlSession.update("updateEmail", emailConfirm);
    }

    // email 인증 코드 삭제
    public int deleteEmailConfirmByEmail(String email) throws SQLException{
        return sqlSession.delete("deleteEmailConfirmByEmail", email);
    }

}