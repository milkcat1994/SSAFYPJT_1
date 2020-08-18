package com.web.editor.model.service.user.search.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.web.editor.model.dto.request.SearchAverageScore;
import com.web.editor.model.dto.request.SearchRequestVideoInfo;
import com.web.editor.model.dto.user.search.SearchPortfolio;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinBookmark;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinVideo;
import com.web.editor.model.dto.user.search.SearchRequest;
import com.web.editor.model.dto.user.search.SearchTag;

public interface SearchRedisService {
    // 북마크수, 포트폴리오 테이블 Join 저장
    void portfolioAndBookmarkSave(List<SearchPortfolioJoinBookmark> searchPortfolioJoinBookmarks);

    // 영상, 포트폴리오 테이블 Join 저장
    void portfolioAndVideoSave(List<SearchPortfolioJoinVideo> searchPortfolioJoinVideos);

    // 요청서, 리뷰 테이블 Join 저장
    // 내부적으로 nickname, 평점 으로 저장
    void requestAndReviewSave(List<SearchAverageScore> searchAverageScores);

    //
    // 위의 Join된 테이블 정보를 이용하여 사용가능한 정보를 만들어야 한다.
    // uid안의 nickname을 이용하여 SearchAverageScore가 저장된 key값을 찾아야 한다. -> 없으면 DB에서 불러오거나 0으로 산정해야한다.
    void makeUserInfo();

    // filter 입력 -> request_form의 video_type, video_style, video_skill
    // video_type, video_style, video_skill - nickname으로 잘라서 저장하기
    void searchRequestVideoInfoSave(List<SearchRequestVideoInfo> searchRequestVideoInfos);

    // tag값 저장
    // tag - uid
    void portfolioTagSave(List<SearchTag> searchTags);

    // 검색결과 도출
    // filter -> video_type, video_style, video_skill
    // 검색어 -> tag, nickname
    // 정렬 -> 가격 낮은, 가격 높은, 이름순, 평점순
    List<SearchPortfolio> getListByFilter(SearchRequest searchRequest);

    // util
    // 패턴에 일치하는 key값들을 찾기 위한 함수들
    // uid-nickname 형태 
    Set<String> hashKeys(String pattern);

    long deleteAll();
    
    long deleteKeys(String pattern);
}