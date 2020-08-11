package com.web.editor.model.mapper.user;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.PortfolioList;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinBookmark;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinVideo;
import com.web.editor.model.dto.user.search.SearchTag;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class SearchMapper {
	
    @Autowired
    @Qualifier("firstSqlSessionTemplate")
	private SqlSession sqlSession;
	
    // @Autowired
    // @Qualifier("SecondSqlSessionTemplate")
	// private SqlSession reqSqlSession;

	// public List<Portfolio> searchAll(Map map) throws SQLException {
	// 	return sqlSession.selectList("searchAll", map);
	// }
	
	// 포트폴리오 - 북마크 
	// uid, nickname, pay_min, skill, count(bookmark)
	public List<SearchPortfolioJoinBookmark> joinBookmarks() throws SQLException{
		return sqlSession.selectList("joinBookmarks");
	}
	
	// 포트폴리오 - 영상
	// uid, mainUrl
	public List<SearchPortfolioJoinVideo> joinVideos() throws SQLException{
		return sqlSession.selectList("joinVideos");
	}

	// 포트폴리오에 등록된 tag
	public List<SearchTag> searchPortfolioTag() throws SQLException{
		return sqlSession.selectList("searchPortfolioTag");
	}
	
	// 아무 검색, 필터 없을때 모든 정보 불러오기-> Redis로만 연동 할건데 필요한가?
	public List<PortfolioList> searchAll() throws SQLException {
		return sqlSession.selectList("searchAll");
	}
	
	public Portfolio search(int uid) throws SQLException {
		return sqlSession.selectOne("search", uid);
	}

	

}
