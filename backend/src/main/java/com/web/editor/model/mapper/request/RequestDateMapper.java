package com.web.editor.model.mapper.request;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.request.RequestDateDto;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class RequestDateMapper {
    @Qualifier("SecondSqlSessionTemplate")
    
    @Autowired
    SqlSession sqlSession;
    
    // 일반회원의 진행중 일정
	public List<RequestDateDto> progressReq(String nickname) throws SQLException{
		return sqlSession.selectList("progressReq", nickname);
	};

	// 편집자의 진행중 일정
	public List<RequestDateDto> progressRes(String nickname) throws SQLException{
		return sqlSession.selectList("progressRes", nickname);
	};
}