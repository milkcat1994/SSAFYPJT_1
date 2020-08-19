package com.web.editor.model.service.request;

import java.util.List;

import com.web.editor.model.dto.request.NicknameUpdateDto;
import com.web.editor.model.dto.request.NotifyDto;
import com.web.editor.model.dto.request.RequestDto;
import com.web.editor.model.dto.request.RequestReview;
import com.web.editor.model.dto.request.RequestReviewSaveRequest;
import com.web.editor.model.dto.request.RequestStatusDto;
import com.web.editor.model.dto.request.RequestTagDto;
import com.web.editor.model.dto.request.SearchAverageScore;
import com.web.editor.model.dto.request.SearchRequestVideoInfo;
import com.web.editor.model.dto.user.PortfolioReview;

public interface RequestService {
	// 요청 검색
	public RequestDto searchRequest(int rid);

	// 요청자의 리스트
	public List<RequestDto> searchListFlagRequest(RequestStatusDto statusDto);

	// 요청 받는 사람의 리스트
	public List<RequestDto> searchListFlagResponse(RequestStatusDto statusDto);

	// 요청 등록
	public int insertRequest(RequestDto requestDto);

	// 요청 수락
	public int acceptRequest(int rid);

	// 요청 거절
	public int denyRequest(int rid);

	// 요청 완료
	public int doneRequest(int rid);

	// 후기 완료
	public int doneReview(int rid);

	// 알림 조회
	public List<NotifyDto> searchNotify(String response_nickname);

	// 알림 등록
	public int insertNotify(NotifyDto notifyDto);

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

	// 리뷰 조회 - 요청서의 리뷰 조회
	public RequestReview searchReview(int rid);

	// 리뷰 모두 조회 - 해당 편집자의 모든 리뷰 조회
	public List<PortfolioReview> searchReviewList(String responseNickname);

	// 리뷰 등록 - 요청서의 리뷰 등록
	public int insertReview(RequestReviewSaveRequest requestReviewSaveRequest);

	// 리뷰 삭제 - 요청서의 리뷰 삭제
	public int deleteReview(int rid);

	// 리뷰 삭제 - 요청서의 상태 변경
	public int deleteReviewAndUpdateRequest(int rid);


	public int updateNicknameReq(NicknameUpdateDto nicknameUpdateDto);
	public int updateNicknameRes(NicknameUpdateDto nicknameUpdateDto);
	public int updateReqNoti(NicknameUpdateDto nicknameUpdateDto);
	public int updateResNoti(NicknameUpdateDto nicknameUpdateDto);
	public int updateNickReview(NicknameUpdateDto nicknameUpdateDto);
	
	// 평점 평균 반환 함수 - Redis 이용하기 위한 테이블 Join
	public List<SearchAverageScore> joinScores();

	// videoType, videoStyle, videoSkill 검색 -> ','로 구분
	public List<SearchRequestVideoInfo> searchRequestVideoInfo();
}
