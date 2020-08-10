package com.web.editor.model.service.user;

import java.util.List;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.PortfolioInfo;
import com.web.editor.model.dto.user.PortfolioNicknameUpdateRequest;
import com.web.editor.model.dto.user.PortfolioTag;
import com.web.editor.model.dto.user.PortfolioTagSaveRequest;
import com.web.editor.model.dto.user.PortfolioReview;
import com.web.editor.model.dto.user.PortfolioReviewSaveRequest;
import com.web.editor.model.dto.user.PortfolioSchedule;
import com.web.editor.model.dto.user.PortfolioScheduleDeleteRequest;
import com.web.editor.model.dto.user.PortfolioScheduleSaveRequest;
import com.web.editor.model.dto.user.PortfolioVideo;
import com.web.editor.model.dto.user.PortfolioVideoDeleteRequest;
import com.web.editor.model.dto.user.PortfolioVideoSaveRequest;

public interface PortfolioService {
    
    // 포트폴리오 조회
    public Portfolio findPortfolioByUid(String uid);
    
    // 영상 조회
    public List<PortfolioVideo> findVideoByUid(String uid);
    
    // 태그 조회
    public List<PortfolioTag> findTagByUid(String uid);
    
    // 스케쥴 조회
    public List<PortfolioSchedule> findScheduleByUid(String uid);
    
    // 리뷰 조회
    // public List<PortfolioReview> findReviewByUid(String uid);
    
    // 포트폴리오 초기 등록
    // uid를 이용해 uid, nickname 설정
    public int portfolioInitSave(String uid);

    // 영상 등록
    public int videoSave(PortfolioVideoSaveRequest portfolioVideoSaveRequest);
    
    // 태그 등록
    public int tagSave(PortfolioTagSaveRequest portfolioTagSaveRequest);
    
    // 스케쥴 등록
    public int scheduleSave(PortfolioScheduleSaveRequest scheduleSaveRequest);

    // 포트폴리오 수정
    public int portfolioUpdate(PortfolioInfo portfolioInfo);
    
    // 스케줄 삭제
    public int deleteSchedule(PortfolioScheduleDeleteRequest PortfolioScheduleDeleteRequest);

    //비디오 모두 삭제
    public int deleteVideos(PortfolioVideoDeleteRequest portfolioVideoDeleteRequest);

    // 태그 모두 삭제
    public int deleteTags(String uid);

    public int portfolioNicknameUpdate(PortfolioNicknameUpdateRequest portfolioNicknameUpdateRequest);
}