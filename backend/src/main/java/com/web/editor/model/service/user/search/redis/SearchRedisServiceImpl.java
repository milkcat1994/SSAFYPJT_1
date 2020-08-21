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
import com.web.editor.model.dto.user.search.SearchPortfolio;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinBookmark;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinVideo;
import com.web.editor.model.dto.user.search.SearchRequest;
import com.web.editor.model.dto.user.search.SearchTag;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class SearchRedisServiceImpl implements SearchRedisService {

    private RedisTemplate<String, Object> redisTemplateHash;
    private RedisTemplate<String, String> redisTemplateSet;
    private RedisTemplate<String, Integer> redisTemplateHashUid;
    // tag 검색량 우선순위 위한 Set
    // private RedisTemplate<String, Object> redisTemplateZSet;
    
    private HashOperations hashOperations;
    private HashOperations hashUidOperations;
    private SetOperations setOperations;
    private SetOperations setResultOperations;
    private RedisOperations redisOperations;
    
    
    private ObjectMapper objectMapper;

    public SearchRedisServiceImpl(
        RedisTemplate<String, Object> redisTemplateHash,
            RedisTemplate<String, String> redisTemplateSet,
            RedisTemplate<String, Integer> redisTemplateHashUid) {
                try {
                    objectMapper = new ObjectMapper();
                    this.redisTemplateHash = redisTemplateHash;
                    this.redisTemplateSet = redisTemplateSet;
                    hashOperations = redisTemplateHash.opsForHash();
            
                    // searchPortfolio 클래스 받기위한 operation
                    this.redisTemplateHash.setValueSerializer(new Jackson2JsonRedisSerializer<>(SearchPortfolio.class));
                    setResultOperations = redisTemplateHash.opsForSet();
            
            
            
                    setOperations = redisTemplateSet.opsForSet();
                    this.redisTemplateHashUid = redisTemplateHashUid;
                    hashUidOperations = redisTemplateHashUid.opsForHash();
            
                    redisOperations = redisTemplateHash.opsForValue().getOperations();
                } catch (RedisConnectionFailureException e) {
                    System.out.println("Redis와 연결이 끊어져 Redis 객체 생성 불가");
                }
    }

    @Override
    public void portfolioAndBookmarkSave(List<SearchPortfolioJoinBookmark> searchPortfolioJoinBookmarks) 
    throws RedisConnectionFailureException{
        // 북마크 수, 포트폴리오 테이블 Join 결과값 "uid:{uid} 를 key로 임시 데이터 저장"
        // 닉네임 -> uid
        // 평점의 경우 0으로 자동 초기화 된다.
        for (SearchPortfolioJoinBookmark searchPortfolioJoinBookmark : searchPortfolioJoinBookmarks) {
            // searchPortfolioJoinBookmark.setInit("", "");
            // System.out.println(searchPortfolioJoinBookmark);
            hashOperations.putAll("uid:" + searchPortfolioJoinBookmark.getUid(), objectMapper.convertValue(searchPortfolioJoinBookmark, Map.class));

            // nickname - uid 기억하는 hash
            hashUidOperations.put("nickname:uid:"+searchPortfolioJoinBookmark.getNickname(), "uid", searchPortfolioJoinBookmark.getUid());

            //모든 포트폴리오 uid 저장
            setOperations.add("uids", searchPortfolioJoinBookmark.getUid());
            setOperations.add("nickname:uids:"+searchPortfolioJoinBookmark.getNickname(), searchPortfolioJoinBookmark.getUid());
        }
        System.out.println("portfolioAndBookmarkSave 완료");
    }

    // mainUrl 해당 포트폴리오에 저장
    @Override
    public void portfolioAndVideoSave(List<SearchPortfolioJoinVideo> searchPortfolioJoinVideos)
    throws RedisConnectionFailureException{
        for (SearchPortfolioJoinVideo searchPortfolioJoinVideo : searchPortfolioJoinVideos) {
            hashOperations.put("uid:" + searchPortfolioJoinVideo.getUid(), "url",
                    searchPortfolioJoinVideo.getMainUrl());
        }
        System.out.println("portfolioAndVideoSave 완료");
    }

    // 닉네임에 따른 리뷰 점수 등록
    @Override
    public void requestAndReviewSave(List<SearchAverageScore> searchAverageScores)
    throws RedisConnectionFailureException{
        for (SearchAverageScore searchAverageScore : searchAverageScores) {
            hashOperations.put("nickname:review:" + searchAverageScore.getNickname(), "nickname", searchAverageScore.getNickname());
            hashOperations.put("nickname:review:" + searchAverageScore.getNickname(), "avgScore", searchAverageScore.getAvgScore());
        }
        System.out.println("requestAndReviewSave 완료");
    }

    // Join된 테이블 정보를 이용하여 사용가능한 새로운 정보를 만들어낸다.
    // 리뷰와 포폴 합치는 작업 필요
    @Override
    public void makeUserInfo()
    throws RedisConnectionFailureException{
        // 모든 리뷰작성된 키를 가져온다.
        // nickname으로 이루어져있음.
        Set<String> reviewKeys = this.hashKeys("nickname:review:*");
        Iterator<String> iter = reviewKeys.iterator();
        String key;

        // 댓글이 없는 포트폴리오의 경우 직접 0으로 설정해주어야 한다. -> portfolioAndBookmarkSave에서 모두 0으로 초기
        // 설정해둔다.
        while (iter.hasNext()) {
            key = iter.next();
            // 해당 nickname키들을 이용해 uid를 가져오고 해당 uid에 해당하는 Hash에 score점수를 넣는다.
            hashOperations.put("uid:"+hashUidOperations.get("nickname:uid:"+hashOperations.get(key, "nickname"), "uid"), "avgScore", hashOperations.get(key, "avgScore"));

            // hashOperations.put(key, "score", hashOperations.get("nickname:" + hashOperations.get(key, "nickname"), "score"));
        }
        System.out.println("makeUserInfo 완료");

        // 모든 정보를 "uid:{uid}"에 담아냈다.
    }

    // filter 입력 -> request_form의 video_type, video_style, video_skill
    // video_type, video_style, video_skill - nickname으로 잘라서 저장하기
    @Override
    public void searchRequestVideoInfoSave(List<SearchRequestVideoInfo> searchRequestVideoInfos)
    throws RedisConnectionFailureException{
        Integer uid;
        StringTokenizer st;
        // nickname:uid:{nickname} 이용하여 nickname를 통해 uid를 알아내자
        for (SearchRequestVideoInfo searchRequestVideoInfo : searchRequestVideoInfos) {
            uid = (Integer) hashUidOperations.get("nickname:uid:" + searchRequestVideoInfo.getNickname(), "uid");
            //각 filter (video_type, video_style, video_skill)에 대해 uid를 가지고 있다.(tag와 비슷한 방식)
            setOperations.add("videoType:"+searchRequestVideoInfo.getVideoType(), uid);
            //기타 영상종류를 거르기 위한 switch문
            switch (searchRequestVideoInfo.getVideoStyle()) {
                case "kids":
                case "game":
                case "musi":
                case "food":
                case "vlog":
                case "movi":
                case "anim":
                case "beau":
                case "spor":
                    setOperations.add("videoStyle:"+searchRequestVideoInfo.getVideoStyle(), uid);
                    break;
            
                case "etcs":
                default:
                    setOperations.add("videoStyle:etcs", uid);
                    break;
            }
            // skill은 String 형태, ','를 기준으로 잘라 넣어야한다.
            st = new StringTokenizer(searchRequestVideoInfo.getVideoSkill(), ",");
            while(st.hasMoreTokens()){
                setOperations.add("videoSkill:"+st.nextToken().trim(), uid);
            }
        }
        System.out.println("searchRequestVideoInfoSave 완료");
    }

    // tag-uid 저장
    // SearchPortfolioJoinBookmark Object에 uid의 태그 Set의 Key를 기억
    @Override
    public void portfolioTagSave(List<SearchTag> searchTags)
    throws RedisConnectionFailureException{
        for (SearchTag searchTag : searchTags) {
            if(searchTag == null) continue;
            // 태그별 uid Set 생성
            setOperations.add("tag:"+searchTag.getTagName(), searchTag.getUid());
            // 각 uid가 가지는 태그 기억할 Set생성
            setOperations.add("tags:"+searchTag.getUid(), searchTag.getTagName());
            
            hashOperations.put("uid:"+searchTag.getUid(), "tagKey", "tags:"+searchTag.getUid());
        }
        // System.out.println(setOperations.members("tags:41"));
        System.out.println("portfolioTagSave 완료");
    }

    
	private String makeKey(SearchRequest searchRequest){
        StringBuilder sb = new StringBuilder();

        if(!searchRequest.getVideoTypes().isEmpty()){
            sb.append("videoType:");
        }
        // videoStyle을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoTypes()) {
            sb.append(string+":");
        }

        if(!searchRequest.getVideoStyles().isEmpty()){
            sb.append("videoStyle:");
        }
        // videoStyle을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoStyles()) {
            sb.append(string+":");
        }
        if(!searchRequest.getVideoSkills().isEmpty()){
            sb.append("videoSkill:");
        }
        // videoStyle을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoSkills()) {
            sb.append(string+":");
        }

        // 없다면 모든 uid가 필요하다.
        if(sb.length()==0) {
            sb.append("uids:all:");
        }

		switch (searchRequest.getSearchType()) {
            case "TAG":
                // 태그 검색이지만 태그 검색어가 없을경우 모두 검색 해야함
                if(searchRequest.getSearchTags().size() <= 1 && searchRequest.getSearchTags().get(0).equals("")) {
                    sb.append("all");
                }
                else{
                    sb.append("tag:");
                    for(String tag : searchRequest.getSearchTags()){
                        sb.append(tag).append(':');
                    }
                }
				break;
                // 닉네임 검색이지만 닉네임 검색어가 없을경우 모두 검색 해야함
            case "NICKNAME":
                if(searchRequest.getSearchText().trim().equals("")) {
                    sb.append("all");
                }
                else{
                    sb.append("nickname:").append(searchRequest.getSearchText());
                }
			break;
            case "ALL":
                sb.append("all");
            break;
			default:
				return "";
        }
		return "search:"+sb.toString();
    }
    
    // 검색결과 도출
    // filter -> video_type, video_style, video_skill
    // 검색어 -> tag, nickname
    // 정렬 -> 가격 낮은, 가격 높은, 이름순, 평점순
    public List<SearchPortfolio> getListByFilter(SearchRequest searchRequest)
    throws RedisConnectionFailureException{
        List<SearchPortfolio> resultList = new ArrayList<>();
        String searchKey = makeKey(searchRequest);
        // System.out.println(searchRequest.getSearchTags().isEmpty());
        // System.out.println(searchRequest.getSearchTags());
        // System.out.println(searchRequest.getSearchTags().size());
        SearchPortfolioJoinBookmark tempResult;
        // Redis에 해당 검색 key값이 있는지 확인
        if(!hashKeys(searchKey).isEmpty()){
            // key가 있다면 해당 key로 값을 가져오기
            // setResultOperations.members(searchKey) 해당 식은 uid를 가져오는 것이다.
            // tag만 검색 했을 때 다른 searchKey를 뱉어야 한다.

            resultList.addAll(setResultOperations.members(searchKey));

            // 정렬 -> 가격 낮은, 가격 높은, 이름순, 평점순
            // 기본 정렬 평점순
            // 정렬
            switch (searchRequest.getSortType()) {
                case "PRICE_ASC":
                    Collections.sort(resultList, (a, b) -> a.getPayMin() - b.getPayMin());
                    break;
                case "PRICE_DESC":
                    Collections.sort(resultList, (a, b) -> b.getPayMin() - a.getPayMin());
                    break;
                case "NICKNAME_ASC":
                    Collections.sort(resultList, (a, b) -> a.getNickname().compareTo(b.getNickname()));
                    break;
                case "SCORE_DESC":
                default:
                    Collections.sort(resultList, (a, b) -> Float.compare(b.getAvgScore(), a.getAvgScore()));
                    break;
            }

            return resultList;
        }

        Set<String> keySet = new LinkedHashSet<>();
        Set<String> videoKeySet = new LinkedHashSet<>();

        // 자료 부르기 위한 Key값 만드는 StringBuilder
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();


        // Video Style, Video Type, Video Skill 안에서는 합집합으로 계산 하며
        // 각각 video 속성은 교집합이다. 
        // 자료 만들기 위한 key값 생성
        if(!searchRequest.getVideoStyles().isEmpty()){
            sb.append("videoStyle:");
            sb2.append("videoStyle:");
        }
        // videoStyle을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoStyles()) {
            keySet.add("videoStyle:"+string);
            sb.append(string+":");
            sb2.append(string+":");
        }

        if(!searchRequest.getVideoStyles().isEmpty()){
            setOperations.unionAndStore(keySet, sb2.toString());
            videoKeySet.add(sb2.toString());
        }
        keySet = new LinkedHashSet<>();
        sb2.setLength(0);

        // 자료 만들기 위한 key값 생성
        if(!searchRequest.getVideoTypes().isEmpty()){
            sb.append("videoType:");
            sb2.append("videoType:");
        }
        // videoType을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoTypes()) {
            keySet.add("videoType:"+string);
            sb.append(string+":");
            sb2.append(string+":");
        }

        if(!searchRequest.getVideoTypes().isEmpty()){
            setOperations.unionAndStore(keySet, sb2.toString());
            videoKeySet.add(sb2.toString());
        }
        keySet = new LinkedHashSet<>();
        sb2.setLength(0);

        // 자료 만들기 위한 key값 생성
        if(!searchRequest.getVideoSkills().isEmpty()){
            sb.append("videoSkill:");
            sb2.append("videoSkill:");
        }
        // videoSkill을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoSkills()) {
            keySet.add("videoSkill:"+string);
            sb.append(string+":");
            sb2.append(string+":");
        }
        if(!searchRequest.getVideoSkills().isEmpty()){
            setOperations.unionAndStore(keySet, sb2.toString());
            videoKeySet.add(sb2.toString());
        }
        keySet = new LinkedHashSet<>();
        sb2.setLength(0);

        // 길이가 1이상이라면 마지막 ':'를 없애야 하기때문에
        if(sb.length()>1) {
            sb.setLength(sb.length()-1);
        }
        // 없다면 모든 uid가 필요하다.
        else{
            keySet.add("uids");
            videoKeySet.add("uids");
            sb.append("uids:all");
        }
        String filterString = sb.toString();
        sb.setLength(0);

        // (videoSkill, videoType, videoStyle)의 교집합 구하기
        if(!videoKeySet.isEmpty())
            setOperations.intersectAndStore(videoKeySet, filterString);
            // setOperations.unionAndStore(keySet, filterString);
            
        // SearchType에 따라 tag, nickname, all 이 갈린다.
        // tag는 정확한 검색 결과 여야 하며
        // nickname은 포함하는 방식으로 *{nickname}* 로 pattern 주면 가능
        keySet = new LinkedHashSet<>();

        switch (searchRequest.getSearchType()) {
            case "TAG":
                // 태그 검색이지만 태그 검색어가 없을경우 모두 검색 해야함
                if(searchRequest.getSearchTags().size() <= 1 && searchRequest.getSearchTags().get(0).equals("")) {
                    sb.append("all");
                }
                else{
                    // 해당 tag를 가지는 모든 uid 더하기
                    sb.append("tag:");
                    for (String tag : searchRequest.getSearchTags()) {
                        keySet.add("tag:"+tag);
                        sb.append(tag).append(':');
                    }
                    sb.setLength(sb.length());
                }
                break;
            case "NICKNAME":
                // 닉네임 검색이지만 닉네임 검색어가 없을경우 모두 검색 해야함
                if(searchRequest.getSearchText().trim().equals("")) {
                    sb.append("all");
                }
                else{
                    sb.append("nickname:").append(searchRequest.getSearchText());
                    //해당 글자 포함하는 닉네임 가지는 Key 모두 저장
                    keySet.addAll(hashKeys("nickname:uids:*"+searchRequest.getSearchText()+"*"));
                    // 검색어가 없을 경우 합치지 않는다.
                    if(!keySet.isEmpty()){
                        setOperations.unionAndStore(keySet, "nickname:uids:search:"+searchRequest.getSearchText());
                    }
                    keySet = new LinkedHashSet<>();
                    keySet.add("nickname:uids:search:"+searchRequest.getSearchText());
                }
            break;
            case "ALL":
                sb.append("all");
                keySet.add("uids");
            break;
            //잘못 들어올 경우 리턴 값 없음.
            default:
                return null;
        }
        // video filter와 검색 결과를 담은 key Set이다.
        keySet.add(filterString);
        String searchString = sb.toString();
        // System.out.println(searchRequest.getSearchTags());
        // System.out.println(searchString);
        sb.setLength(0);
        // 검색 결과 uid를 담는 Set이다. -> 교집합 통해 uid집합 만들기
        // nickname:uid:{nickname} 가 Hash이므로 intersect연산이 되지 않는다.
        Set<String> resultSet = setOperations.intersect(keySet);

        // 모든 검색 UID를 돌면서 리스트를 띄우기 위한 양식으로 만든다.
        for (String uid : resultSet) {
            tempResult = objectMapper.convertValue(hashOperations.entries("uid:"+uid), SearchPortfolioJoinBookmark.class);
            
            // tagKey를 이용하여 tagList를 구해야 한다.
            if(tempResult.getTagKey() == null){
                setResultOperations.add("search:"+filterString+":"+searchString, new SearchPortfolio(tempResult, Collections.emptySet()));
            }
            else{
                setResultOperations.add("search:"+filterString+":"+searchString, new SearchPortfolio(tempResult, setOperations.members(tempResult.getTagKey())));
            }
        }
        
        // 검색어시 key로 추가해 넣어야한다.+searchString);
        resultList.addAll(setResultOperations.members("search:"+filterString+":"+searchString));

        // 정렬 -> 가격 낮은, 가격 높은, 이름순, 평점순
        // 기본 정렬 평점순
        // 정렬
        switch (searchRequest.getSortType()) {
            case "PRICE_ASC":
                Collections.sort(resultList, (a, b) -> a.getPayMin() - b.getPayMin());
                break;
            case "PRICE_DESC":
                Collections.sort(resultList, (a, b) -> b.getPayMin() - a.getPayMin());
                break;
            case "NICKNAME_ASC":
                Collections.sort(resultList, (a, b) -> a.getNickname().compareTo(b.getNickname()));
                break;
            case "SCORE_DESC":
            default:
                Collections.sort(resultList, (a, b) -> Float.compare(b.getAvgScore(), a.getAvgScore()));
                break;
        }

        return resultList;
    }

    // keys의 패턴을 통해 모든 key를 모두 불러오자.
    @Override
    public Set<String> hashKeys(String pattern) {
        return this.redisTemplateHash.keys(pattern);
    }

    @Override
    public long deleteAll()
    throws RedisConnectionFailureException{
        Set<String> allKeys = this.hashKeys("*");
        return redisOperations.delete(allKeys);
    }

    @Override
    public long deleteKeys(String pattern)
    throws RedisConnectionFailureException{
        Set<String> searchKeys = this.hashKeys(pattern+"*");
        return redisOperations.delete(searchKeys);
    }
    
}