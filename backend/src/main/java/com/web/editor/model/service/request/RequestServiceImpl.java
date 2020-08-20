package com.web.editor.model.service.request;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.web.editor.model.mapper.request.RequestMapper;

@Service
public class RequestServiceImpl implements RequestService {

	@Autowired
	private RequestMapper requestMapper;

	// 요청서
	// 요청서 상세 검색
	@Override
	public RequestDto searchRequest(int rid) {
		try {
			return requestMapper.searchRequest(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 요청서 리스트(일반회원)
	@Override
	public List<RequestDto> searchListFlagRequest(RequestStatusDto statusDto) {
		try {
			return requestMapper.searchListFlagRequest(statusDto);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 요청서 리스트(편집자)
	@Override
	public List<RequestDto> searchListFlagResponse(RequestStatusDto statusDto) {
		try {
			return requestMapper.searchListFlagResponse(statusDto);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 요청서 등록
	@Override
	public int insertRequest(RequestDto requestDto) {
		try {
			return requestMapper.insertRequest(requestDto);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	// 요청 수락
	@Override
	public int acceptRequest(int rid) {
		try {
			return requestMapper.acceptRequest(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 요청 완료
	@Override
	public int doneRequest(int rid) {
		try {
			return requestMapper.doneRequest(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 후기 완료
	@Override
	public int doneReview(int rid) {
		try {
			return requestMapper.doneReview(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 요청 거절
	@Override
	public int denyRequest(int rid) {
		try {
			return requestMapper.denyRequest(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 알림
	// 요청 알림리스트 조회(일반회원)
	@Override
	public List<NotifyDto> searchNotify(String response_nickname) {
		try {
			return requestMapper.searchNotify(response_nickname);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	// 알림 등록
	@Override
	public int insertNotify(NotifyDto notifyDto) {
		try {
			return requestMapper.insertNotify(notifyDto);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	// 알림 수정(읽음)
	@Override
	public int updateNotify(int nid) {
		try {
			return requestMapper.updateNotify(nid);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	// 알림 수정2(회원이 알림을 모두 읽음)
	@Override
	public int updateAllNotify(String response_nickname) {
		try {
			return requestMapper.updateAllNotify(response_nickname);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 태그
	// 해당 요청서의 태그 검색
	@Override
	public List<RequestTagDto> searchTag(int rid) {
		try {
			return requestMapper.searchTag(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	// 태그 등록
	@Override
	public int insertTag(RequestTagDto requestTagDto) {
		try {
			return requestMapper.insertTag(requestTagDto);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	// 해당 요청서의 태그 삭제
	@Override
	public int deleteReqTag(int rid) {
		try {
			return requestMapper.deleteReqTag(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 해당 요청서의 리뷰 조회
	@Override
	public RequestReview searchReview(int rid) {
		try {
			return requestMapper.searchReview(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PortfolioReview> searchReviewList(String responseNickname) {
		try {
			return requestMapper.searchReviewList(responseNickname);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 해당 요청서에 리뷰 등록
	@Override
	public int insertReview(RequestReviewSaveRequest requestReviewSaveRequest) {
		try {
			return requestMapper.insertReview(requestReviewSaveRequest);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 해당 요청서의 리뷰 삭제
	@Override
	public int deleteReview(int rid) {
		try {
			return requestMapper.deleteReview(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int deleteReviewAndUpdateRequest(int rid) {
		try {
			return requestMapper.deleteReviewAndUpdateRequest(rid);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int updateNicknameReq(NicknameUpdateDto nicknameUpdateDto) {
		try {
			return requestMapper.updateNicknameReq(nicknameUpdateDto);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int updateNicknameRes(NicknameUpdateDto nicknameUpdateDto) {
		try {
			return requestMapper.updateNicknameRes(nicknameUpdateDto);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int updateReqNoti(NicknameUpdateDto nicknameUpdateDto) {
		try {
			return requestMapper.updateReqNoti(nicknameUpdateDto);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int updateResNoti(NicknameUpdateDto nicknameUpdateDto) {
		try {
			return requestMapper.updateResNoti(nicknameUpdateDto);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public List<SearchAverageScore> joinScores() {
		List<SearchAverageScore> list = new ArrayList<>();
		try {
			return requestMapper.joinScores();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<SearchRequestVideoInfo> searchRequestVideoInfo() {
		List<SearchRequestVideoInfo> list = new ArrayList<>();
		try {
			return requestMapper.searchRequestVideoInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int updateNickReview(NicknameUpdateDto nicknameUpdateDto) {
		try {
			return requestMapper.updateNickReview(nicknameUpdateDto);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}


}
