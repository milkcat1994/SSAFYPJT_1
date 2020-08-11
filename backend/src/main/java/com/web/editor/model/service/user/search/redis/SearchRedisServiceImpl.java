package com.web.editor.model.service.user.search.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.editor.model.dto.request.SearchAverageScore;
import com.web.editor.model.dto.request.SearchRequestVideoInfo;
import com.web.editor.model.dto.user.PortfolioTag;
import com.web.editor.model.dto.user.search.SearchPortfolio;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinBookmark;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinVideo;
import com.web.editor.model.dto.user.search.SearchRequest;
import com.web.editor.model.dto.user.search.SearchTag;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class SearchRedisServiceImpl implements SearchRedisService {

    private RedisTemplate<String, Object> redisTemplateHash;
    private RedisTemplate<String, Object> redisTemplateSet;
    // tag 검색량 우선순위 위한 Set
    // private RedisTemplate<String, Object> redisTemplateZSet;

    private HashOperations hashOperations;
    private SetOperations setOperations;

    private ObjectMapper objectMapper;

    public SearchRedisServiceImpl(RedisTemplate<String, Object> redisTemplateHash,
            RedisTemplate<String, Object> redisTemplateSet) {
        this.redisTemplateHash = redisTemplateHash;
        this.redisTemplateSet = redisTemplateSet;
        hashOperations = redisTemplateHash.opsForHash();
        setOperations = redisTemplateSet.opsForSet();
    }

    @Override
    public void portfolioAndBookmarkSave(List<SearchPortfolioJoinBookmark> searchPortfolioJoinBookmarks) {
        // 북마크 수, 포트폴리오 테이블 Join 결과값 "uid:{uid} 를 key로 임시 데이터 저장"
        // 닉네임 -> uid
        // 평점의 경우 0으로 자동 초기화 된다.
        for (SearchPortfolioJoinBookmark searchPortfolioJoinBookmark : searchPortfolioJoinBookmarks) {
            hashOperations.putAll("uid:" + searchPortfolioJoinBookmark.getUid(),
                    objectMapper.convertValue(searchPortfolioJoinBookmark, Map.class));

            // nickname - uid 기억하는 hash
            hashOperations.put("nickname:uid:"+searchPortfolioJoinBookmark.getNickname(), "uid", searchPortfolioJoinBookmark.getUid());

            //모든 포트폴리오 uid 저장
            setOperations.add("uids", searchPortfolioJoinBookmark.getUid());
        }
    }

    // mainUrl 해당 포트폴리오에 저장
    @Override
    public void portfolioAndVideoSave(List<SearchPortfolioJoinVideo> searchPortfolioJoinVideos) {
        for (SearchPortfolioJoinVideo searchPortfolioJoinVideo : searchPortfolioJoinVideos) {
            hashOperations.put("uid:" + searchPortfolioJoinVideo.getUid(), "mainUrl",
                    searchPortfolioJoinVideo.getMainUrl());
        }
    }

    // 닉네임에 따른 리뷰 점수 등록
    @Override
    public void requestAndReviewSave(List<SearchAverageScore> searchAverageScores) {
        for (SearchAverageScore searchAverageScore : searchAverageScores) {
            hashOperations.put("nickname:review:" + searchAverageScore.getNickname(), "avgScore",
                    searchAverageScore.getAvgScore());
        }
    }

    // Join된 테이블 정보를 이용하여 사용가능한 새로운 정보를 만들어낸다.
    // 리뷰와 포폴 합치는 작업 필요
    @Override
    public void makeUserInfo() {
        // 모든 리뷰작성된 키를 가져온다.
        // nickname으로 이루어져있음.
        Set<String> reviewKeys = this.hashKeys("nickname:review:");
        Iterator<String> iter = reviewKeys.iterator();
        String key;

        // 댓글이 없는 포트폴리오의 경우 직접 0으로 설정해주어야 한다. -> portfolioAndBookmarkSave에서 모두 0으로 초기
        // 설정해둔다.
        while (iter.hasNext()) {
            key = iter.next();
            // 해당 nickname키들을 이용해 uid를 가져오고 해당 uid에 해당하는 Hash에 score점수를 넣는다.
            hashOperations.put("uid:"+hashOperations.get("nickname:uid:"+hashOperations.get(key, "nickname"), "uid"), "avgScore", hashOperations.get(key, "avgScore"));

            // hashOperations.put(key, "score", hashOperations.get("nickname:" + hashOperations.get(key, "nickname"), "score"));
        }

        // 모든 정보를 "uid:{uid}"에 담아냈다.
    }

    // filter 입력 -> request_form의 video_type, video_style, video_skill
    // video_type, video_style, video_skill - nickname으로 잘라서 저장하기
    @Override
    public void searchRequestVideoInfoSave(List<SearchRequestVideoInfo> searchRequestVideoInfos) {
        String uid;
        StringTokenizer st;
        // nickname:uid:{nickname} 이용하여 nickname를 통해 uid를 알아내자
        for (SearchRequestVideoInfo searchRequestVideoInfo : searchRequestVideoInfos) {
            uid = (String) hashOperations.get("nickname:uid:"+searchRequestVideoInfo.getNickname(), "uid");
            //각 filter (video_type, video_style, video_skill)에 대해 uid를 가지고 있다.(tag와 비슷한 방식)
            setOperations.add("videoType:"+searchRequestVideoInfo.getVideoType(), uid);
            setOperations.add("videoStyle:"+searchRequestVideoInfo.getVideoStyle(), uid);
            // skill은 String 형태, ','를 기준으로 잘라 넣어야한다.
            st = new StringTokenizer(searchRequestVideoInfo.getVideoSkill(), ",");
            while(st.hasMoreTokens()){
                setOperations.add("videoSkill:"+st.nextToken(), uid);
            }
        }
    }

    // tag-uid 저장
    // SearchPortfolioJoinBookmark Object에 uid의 태그 Set의 Key를 기억
    @Override
    public void portfolioTagSave(List<SearchTag> searchTags){
        for (SearchTag searchTag : searchTags) {
            // 태그별 uid Set 생성
            setOperations.add("tag:"+searchTag.getTag(), searchTag.getUid());
            // 태그 기억할 Set생성
            setOperations.add("tags:"+searchTag.getUid(), searchTag.getTag());
            // hashOperations.get("uid:"+searchTag.getUid(), "tags");
        }
    }

    // 검색결과 도출
    // filter -> video_type, video_style, video_skill
    // 검색어 -> tag, nickname
    // 정렬 -> 가격 낮은, 가격 높은, 이름순, 평점순
    public List<SearchPortfolio> getListByFilter(SearchRequest searchRequest){
        // videoType, videoStyle -> 1개
        Set<String> keySet = new LinkedHashSet<>();
        String newKey;
        if(searchRequest.getVideoType().equals("")){
            // 필터 둘다 없을 경우
            if(searchRequest.getVideoStyle().equals("")){
                // videoUidSet은 모든 uid를 가질 수 있다
                newKey = "uids:all";
                // videoUidSet = setOperations.members(newKey);
                if(setOperations.members(newKey).isEmpty()){
                    keySet.add("uids");
                    setOperations.unionAndStore(keySet, newKey);
                }
                // videoUidSet = setOperations.members(newKey);
            }
            // videoStyle만 적용할 경우
            else{
                newKey = "uids:"+searchRequest.getVideoStyle();
                // videoUidSet = setOperations.members(newKey);
                if(setOperations.members(newKey).isEmpty()){
                    keySet.add("videoStyle:"+searchRequest.getVideoStyle());
                    setOperations.unionAndStore(keySet, newKey);
                }
                // videoUidSet = setOperations.members(newKey);
            }
        }
        else{
            // videoType만 적용할 경우
            if(searchRequest.getVideoStyle().equals("")){
                newKey = "uids:"+searchRequest.getVideoType();
                // videoUidSet = setOperations.members(newKey);
                if(setOperations.members(newKey).isEmpty()){
                    keySet.add("videoType:"+searchRequest.getVideoType());
                    setOperations.unionAndStore(keySet, newKey);
                }
                // videoUidSet = setOperations.members(newKey);
            }
            // videoStyle, videoType 둘다 적용할 경우
            else{
                newKey = "uids:"+searchRequest.getVideoStyle()+":"+searchRequest.getVideoType();
                // videoUidSet = setOperations.members(newKey);
                if(setOperations.members(newKey).isEmpty()){
                    keySet.add("videoStyle:"+searchRequest.getVideoStyle());
                    keySet.add("videoType:"+searchRequest.getVideoType());
                    setOperations.intersectAndStore(keySet, newKey);
                }
                // videoUidSet = setOperations.members(newKey);
                // videoUidSet = setOperations.intersect("videoStyle:"+searchRequest.getVideoStyle(), "videoType:"+searchRequest.getVideoType());
            }
        }

        // videoSkill을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoSkills()) {
            keySet.add("videoSkill:"+string);
        }
        // keySet.addAll(searchRequest.getVideoSkills());
        StringBuilder sb = new StringBuilder();
        for (String string : keySet) {
            sb.append(string).append(':');
        }
        sb.setLength(sb.length()-1);
        String filterString = sb.toString();
        sb.setLength(0);

        // 교집합 구하기
        // videoSkill -> 여러개
        setOperations.intersectAndStore(keySet, filterString);
        // sb.toString()으로 video filter 가능
        // videoUidSet = setOperations.members(sb.toString());

        // for (String videoSkill : searchRequest.getVideoSkills()) {
        //     videoUidSet.retainAll(setOperations.members("videoSkill:"+videoSkill));
        // }

        // SearchType에 따라 tag, nickname 이 갈린다.
        // tag는 정확한 검색 결과 여야 하며
        // nickname은 포함? 하는거두 가능하나? *{nickname}* 로 pattern 주면 가능
        // Set<String> typeUidSet = Collections.emptySet();
        // Set<String> videoUidSet;
        keySet = new LinkedHashSet<>();

        switch (searchRequest.getSearchType()) {
            case TAG:
                // 해당 tag를 가지는 모든 uid 더하기
                for (String tag : searchRequest.getSearchTags()) {
                    keySet.add("tag:"+tag);
                    // typeUidSet.addAll(setOperations.members("tag:"+tag));
                }
                break;
            case NICKNAME:
                keySet = hashKeys(searchRequest.getSearchText());
                // for (String nickname : nicknameSet) {
                //     typeUidSet.addAll(setOperations.members("nickname:uid:*"+nickname+"*"));
                // }
            break;
        }
        keySet.add(filterString);
        // 검색 결과 uid를 담는 Set이다.
        Set<String> resultSet = setOperations.intersect(keySet);
        List<SearchPortfolio> resultList = new ArrayList<>();
        for (String string : resultSet) {
            resultList.add((SearchPortfolio)hashOperations.entries(string));
        }
        // videoUidSet에 중복되는 Uid가 저장된다.
        // videoUidSet.retainAll(typeUidSet);

        // setOperations
        // 정렬 -> 가격 낮은, 가격 높은, 이름순, 평점순
        // SortQuery<String> sortQuery = SortQueryBuilder.sort("key").by("hash*->nickname").build();
        // 정렬 보류



        // 검색될 uid 집합
        // for (String uid : videoUidSet) {
            
        // }
        return resultList;
    }


    @Override
    public Map<Integer, SearchPortfolio> findAllUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Integer> findUidByTag(List<String> tags) {
        // TODO Auto-generated method stub
        return null;
    }

    // keys의 패턴을 통해 모든 key를 모두 불러오자.
    @Override
    public Set<String> hashKeys(String pattern) {
        return this.redisTemplateHash.keys(pattern);
    }


    
}