package com.web.editor.model.service.request;

import java.util.List;

import com.web.editor.model.dto.request.NotifyDto;
import com.web.editor.model.dto.request.RequestDto;
import com.web.editor.model.dto.request.RequestStatusDto;
import com.web.editor.model.dto.request.RequestTagDto;

public interface RequestService {
	// 요청 검색
	public RequestDto searchRequest(int rid);

	// 요청자의 리스트
	public List<RequestDto> searchListRequest(String request_nickname);
	public List<RequestDto> searchListFlagRequest(RequestStatusDto statusDto);

	// 요청 받는 사람의 리스트
	public List<RequestDto> searchListResponse(String response_nickname);
	public List<RequestDto> searchListFlagResponse(RequestStatusDto statusDto);

	// 요청 등록
	public int insertRequest(RequestDto requestDto);

	// 요청 수락
	public int acceptRequest(int rid);

	// 요청 거절
	public int denyRequest(int rid);

	// 요청 완료
	public int doneRequest(int rid);

	// 요청 삭제
	public int deleteRequest(int rid);

	// 알림 조회
	public List<NotifyDto> searchNotify(String response_nickname);	

	// 알림 등록
	public int insertNotify(NotifyDto notifyDto);

	// 알림 삭제
	public int deleteNotify(int nid);

	// 알림 수정(읽음)
	public int updateNotify(int nid);
	// 알림수정2 (회원이 알림을 모두 읽음)
	public int updateAllNotify(String response_nickname);

	// 태그 조회
	public List<RequestTagDto> searchTag(int rid);

	// 태그 등록
	public int insertTag(RequestTagDto requestTagDto);
	
	// 태그 삭제
	public int deleteReqTag(int rid);
}
