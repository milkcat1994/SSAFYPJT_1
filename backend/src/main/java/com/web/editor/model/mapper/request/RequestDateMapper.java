package com.web.editor.model.mapper.request;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.web.editor.model.dto.request.NotifyDto;
import com.web.editor.model.dto.request.RequestDto;
import com.web.editor.model.dto.request.RequestStatusDto;
import com.web.editor.model.dto.request.RequestTagDto;


@Repository
public class RequestDateMapper {
	@Qualifier("SecondSqlSessionTemplate")
	@Autowired
	private SqlSession sqlSession;

	// 요청 검색return sqlSession.selectOne("normalLogin", normalLoginRequest);
	public RequestDto searchRequest(int rid) throws SQLException{
		return sqlSession.selectOne("searchRequest", rid);
	};

	// 요청자의 리스트
	public List<RequestDto> searchListRequest(String request_nickname) throws SQLException{
		return sqlSession.selectList("searchListRequest", request_nickname);
	};
	// nickname, done_flag
	public List<RequestDto> searchListFlagRequest(RequestStatusDto statusDto) throws SQLException{
		return sqlSession.selectList("searchListFlagRequest", statusDto);
	};
	
	// 요청 받는 사람의 리스트
	public List<RequestDto> searchListResponse(String response_nickname) throws SQLException{
		return sqlSession.selectList("searchListResponse", response_nickname);
	};
	public List<RequestDto> searchListFlagResponse(RequestStatusDto statusDto) throws SQLException{
		return sqlSession.selectList("searchListFlagResponse", statusDto);
	};

	// 요청 등록
	public int insertRequest(RequestDto requestDto) throws SQLException{
		return sqlSession.insert("insertRequest", requestDto);

	};

	// 요청 수락
	public int acceptRequest(int rid) throws SQLException{
		return sqlSession.update("acceptRequest", rid);
	};
	// 요청 완료
	public int doneRequest(int rid) throws SQLException{
		return sqlSession.update("doneRequest", rid);
	};
	// 요청 거절
	public int denyRequest(int rid) throws SQLException{
		return sqlSession.update("denyRequest", rid);
	};
	
	// 요청 삭제
	public int deleteRequest(int rid) throws SQLException {
		return sqlSession.delete("deleteRequest", rid);
	};
	
	
	// 알림 조회
	public List<NotifyDto> searchNotify(String response_nickname) throws SQLException{
		return sqlSession.selectList("searchNotify", response_nickname);
	};

	// 알람 등록
	public int insertNotify(NotifyDto notifyDto) throws SQLException{
		return sqlSession.insert("insertNotify", notifyDto);
	};
	
	// 알림 삭제
	public int deleteNotify(int nid) throws SQLException{
		return sqlSession.delete("deleteNotify", nid);
	};
	
	// 알림 수정 (읽음)
	public int updateNotify(int nid) throws SQLException{
		return sqlSession.update("updateNotify", nid);
	};
	// 알림수정2(회원이 알림을 모두 읽음)
	public int updateAllNotify(String response_nickname) throws SQLException {
		return sqlSession.update("updateAllNotify", response_nickname);
	}

	// 태그 조회
	public List<RequestTagDto> searchTag(int rid) throws SQLException{
		return sqlSession.selectList("searchTag", rid);
	};

	// 태그 등록
	public int insertTag(RequestTagDto requestTagDto) throws SQLException{
		return sqlSession.insert("insertTag", requestTagDto);
	};

	// 태그 삭제 - 요청서의 태그 삭제
	public int deleteReqTag(int rid) throws SQLException{
		return sqlSession.delete("deleteReqTag", rid);
	};

}
