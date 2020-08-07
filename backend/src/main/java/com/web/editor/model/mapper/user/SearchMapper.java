package com.web.editor.model.mapper.user;

import java.sql.SQLException;
import java.util.List;

import java.util.Map;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.PortfolioList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class SearchMapper {
	
    @Autowired
    @Qualifier("firstSqlSessionTemplate")
	private SqlSession sqlSession;
	
	// public List<Portfolio> searchAll(Map map) throws SQLException {
	// 	return sqlSession.selectList("searchAll", map);
	// }
	public List<PortfolioList> searchAll() throws SQLException {
		return sqlSession.selectList("searchAll");
	}
	
	public Portfolio search(int uid) throws SQLException {
		return sqlSession.selectOne("search", uid);
	}


}
