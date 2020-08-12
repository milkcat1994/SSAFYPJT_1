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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
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
                objectMapper = new ObjectMapper();
        this.redisTemplateHash = redisTemplateHash;
        this.redisTemplateSet = redisTemplateSet;
        hashOperations = redisTemplateHash.opsForHash();

        this.redisTemplateHash.setValueSerializer(new Jackson2JsonRedisSerializer<>(SearchPortfolio.class));
        setResultOperations = redisTemplateHash.opsForSet();

        setOperations = redisTemplateSet.opsForSet();
        this.redisTemplateHashUid = redisTemplateHashUid;
        hashUidOperations = redisTemplateHashUid.opsForHash();

        redisOperations = redisTemplateHash.opsForValue().getOperations();
    }

    @Override
    public void portfolioAndBookmarkSave(List<SearchPortfolioJoinBookmark> searchPortfolioJoinBookmarks) {
        // 북마크 수, 포트폴리오 테이블 Join 결과값 "uid:{uid} 를 key로 임시 데이터 저장"
        // 닉네임 -> uid
        // 평점의 경우 0으로 자동 초기화 된다.
        for (SearchPortfolioJoinBookmark searchPortfolioJoinBookmark : searchPortfolioJoinBookmarks) {
            // searchPortfolioJoinBookmark.setInit("", "");
            // System.out.println(searchPortfolioJoinBookmark);
            hashOperations.putAll("uid:" + searchPortfolioJoinBookmark.getUid(),
                    objectMapper.convertValue(searchPortfolioJoinBookmark, Map.class));

            // nickname - uid 기억하는 hash
            hashUidOperations.put("nickname:uid:"+searchPortfolioJoinBookmark.getNickname(), "uid", searchPortfolioJoinBookmark.getUid());

            //모든 포트폴리오 uid 저장
            setOperations.add("uids", searchPortfolioJoinBookmark.getUid());
        }
        System.out.println("portfolioAndBookmarkSave 완료");
    }

    // mainUrl 해당 포트폴리오에 저장
    @Override
    public void portfolioAndVideoSave(List<SearchPortfolioJoinVideo> searchPortfolioJoinVideos) {
        for (SearchPortfolioJoinVideo searchPortfolioJoinVideo : searchPortfolioJoinVideos) {
            hashOperations.put("uid:" + searchPortfolioJoinVideo.getUid(), "url",
                    searchPortfolioJoinVideo.getMainUrl());
        }
        System.out.println("portfolioAndVideoSave 완료");
    }

    // 닉네임에 따른 리뷰 점수 등록
    @Override
    public void requestAndReviewSave(List<SearchAverageScore> searchAverageScores) {
        for (SearchAverageScore searchAverageScore : searchAverageScores) {
            hashOperations.put("nickname:review:" + searchAverageScore.getNickname(), "avgScore",
                    searchAverageScore.getAvgScore());
        }
        System.out.println("requestAndReviewSave 완료");
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
            hashOperations.put("uid:"+hashUidOperations.get("nickname:uid:"+hashOperations.get(key, "nickname"), "uid"), "avgScore", hashOperations.get(key, "avgScore"));

            // hashOperations.put(key, "score", hashOperations.get("nickname:" + hashOperations.get(key, "nickname"), "score"));
        }
        System.out.println("makeUserInfo 완료");

        // 모든 정보를 "uid:{uid}"에 담아냈다.
    }

    // filter 입력 -> request_form의 video_type, video_style, video_skill
    // video_type, video_style, video_skill - nickname으로 잘라서 저장하기
    @Override
    public void searchRequestVideoInfoSave(List<SearchRequestVideoInfo> searchRequestVideoInfos) {
        Integer uid;
        StringTokenizer st;
        // nickname:uid:{nickname} 이용하여 nickname를 통해 uid를 알아내자
        for (SearchRequestVideoInfo searchRequestVideoInfo : searchRequestVideoInfos) {
            uid = (Integer) hashUidOperations.get("nickname:uid:" + searchRequestVideoInfo.getNickname(), "uid");
            //각 filter (video_type, video_style, video_skill)에 대해 uid를 가지고 있다.(tag와 비슷한 방식)
            setOperations.add("videoType:"+searchRequestVideoInfo.getVideoType(), uid);
            setOperations.add("videoStyle:"+searchRequestVideoInfo.getVideoStyle(), uid);
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
    public void portfolioTagSave(List<SearchTag> searchTags){
        for (SearchTag searchTag : searchTags) {
            // 태그별 uid Set 생성
            setOperations.add("tag:"+searchTag.getTag(), searchTag.getUid());
            // 각 uid가 가지는 태그 기억할 Set생성
            setOperations.add("tags:"+searchTag.getUid(), searchTag.getTag());
            
            hashOperations.putIfAbsent("uid:"+searchTag.getUid(), "tagKey", "tags:"+searchTag.getUid());
        }
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


		switch (searchRequest.getSearchType()) {
			case TAG:
				sb.append("tag:").append(searchRequest.getSearchTags().get(0));
				break;
			case NICKNAME:
				sb.append("nickname:").append(searchRequest.getSearchText());
			break;
			default:
				return "";
        }
		return sb.toString();
    }
    
    // 검색결과 도출
    // filter -> video_type, video_style, video_skill
    // 검색어 -> tag, nickname
    // 정렬 -> 가격 낮은, 가격 높은, 이름순, 평점순
    public List<SearchPortfolio> getListByFilter(SearchRequest searchRequest){
        List<SearchPortfolio> resultList = new ArrayList<>();
        String searchKey = makeKey(searchRequest);
        if(searchKey.isEmpty()){
            System.out.println("key가 없음");
            // return resultList;
        }
        else{
            // Set<SearchPortfolio> tempSet = setOperations.members(searchKey);
            //비면 찾기 아니면 만들기
            resultList.addAll(setResultOperations.members(searchKey));
            System.out.println("캐시에서 가져옴");
            return resultList;
        }

        // videoType, videoStyle -> 1개
        Set<String> keySet = new LinkedHashSet<>();
        String newKey;
        // if(searchRequest.getVideoType().equals("")){
        //     // 필터 둘다 없을 경우
        //     if(searchRequest.getVideoStyle().equals("")){
        //         // videoUidSet은 모든 uid를 가질 수 있다
        //         newKey = "uids:all";
        //         // videoUidSet = setOperations.members(newKey);
        //         if(setOperations.members(newKey).isEmpty()){
        //             keySet.add("uids");
        //             setOperations.unionAndStore(keySet, newKey);
        //         }
        //         // videoUidSet = setOperations.members(newKey);
        //     }
        //     // videoStyle만 적용할 경우
        //     else{
        //         newKey = "uids:"+searchRequest.getVideoStyle();
        //         // videoUidSet = setOperations.members(newKey);
        //         if(setOperations.members(newKey).isEmpty()){
        //             keySet.add("videoStyle:"+searchRequest.getVideoStyle());
        //             setOperations.unionAndStore(keySet, newKey);
        //         }
        //         // videoUidSet = setOperations.members(newKey);
        //     }
        // }
        // else{
        //     // videoType만 적용할 경우
        //     if(searchRequest.getVideoStyle().equals("")){
        //         newKey = "uids:"+searchRequest.getVideoType();
        //         // videoUidSet = setOperations.members(newKey);
        //         if(setOperations.members(newKey).isEmpty()){
        //             keySet.add("videoType:"+searchRequest.getVideoType());
        //             setOperations.unionAndStore(keySet, newKey);
        //         }
        //         // videoUidSet = setOperations.members(newKey);
        //     }
        //     // videoStyle, videoType 둘다 적용할 경우
        //     else{
        //         newKey = "uids:"+searchRequest.getVideoStyle()+":"+searchRequest.getVideoType();
        //         // videoUidSet = setOperations.members(newKey);
        //         if(setOperations.members(newKey).isEmpty()){
        //             keySet.add("videoStyle:"+searchRequest.getVideoStyle());
        //             keySet.add("videoType:"+searchRequest.getVideoType());
        //             setOperations.intersectAndStore(keySet, newKey);
        //         }
        //         // videoUidSet = setOperations.members(newKey);
        //         // videoUidSet = setOperations.intersect("videoStyle:"+searchRequest.getVideoStyle(), "videoType:"+searchRequest.getVideoType());
        //     }
        // }
        keySet = new LinkedHashSet<>();
        // keySet.add(newKey);
        System.out.println("1차 완료");

        // 자료 부르기 위한 Key값 만드는 StringBuilder
        StringBuilder sb = new StringBuilder();


        // 자료 만들기 위한 key값 생성
        if(!searchRequest.getVideoStyles().isEmpty()){
            sb.append("videoStyle:");
        }
        // videoStyle을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoStyles()) {
            keySet.add("videoStyle:"+string);
            sb.append(string+":");
        }


        // 자료 만들기 위한 key값 생성
        if(!searchRequest.getVideoTypes().isEmpty()){
            sb.append("videoType:");
        }
        // videoType을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoTypes()) {
            keySet.add("videoType:"+string);
            sb.append(string+":");
        }



        // 자료 만들기 위한 key값 생성
        if(!searchRequest.getVideoSkills().isEmpty()){
            sb.append("videoSkill:");
        }
        // videoSkill을 모두 돌면서 해당되는 key값을 더해주기
        for (String string : searchRequest.getVideoSkills()) {
            keySet.add("videoSkill:"+string);
            sb.append(string+":");
        }
        // keySet.addAll(searchRequest.getVideoSkills());
        // 기존 key값 더해주기
        // sb.append(newKey).append(':');
        // for (String string : keySet) {
        //     sb.append(string).append(':');
        // }

        // 길이가 1이상이라면 마지막 ':'를 없애야 하기때문에
        if(sb.length()>1) {
            sb.setLength(sb.length()-1);
        }
        // 없다면 모든 uid가 필요하다.
        else{
            keySet.add("uids");
            sb.append("uids:all");
        }
        String filterString = sb.toString();
        sb.setLength(0);

        // (videoSkill, videoType, videoStyle)의 합집합 구하기
        if(!keySet.isEmpty())
            setOperations.unionAndStore(keySet, filterString);
        System.out.println("2차 완료"+filterString);

        // SearchType에 따라 tag, nickname 이 갈린다.
        // tag는 정확한 검색 결과 여야 하며
        // nickname은 포함? 하는거두 가능하나? *{nickname}* 로 pattern 주면 가능
        // Set<String> typeUidSet = Collections.emptySet();
        // Set<String> videoUidSet;
        keySet = new LinkedHashSet<>();

        switch (searchRequest.getSearchType()) {
            case TAG:
                // 해당 tag를 가지는 모든 uid 더하기
                sb.append("tag:");
                for (String tag : searchRequest.getSearchTags()) {
                    keySet.add("tag:"+tag);
                    sb.append(tag).append(':');
                    // typeUidSet.addAll(setOperations.members("tag:"+tag));
                }
                sb.setLength(sb.length());
                break;
            case NICKNAME:
                sb.append("nickname:").append(searchRequest.getSearchText());
                //해당 글자 포함하는 닉네임 가지는 Key 모두 저장
                keySet.addAll(hashKeys("nickname:uid:*"+searchRequest.getSearchText()+"*"));
                // for (String nickname : nicknameSet) {
                //     typeUidSet.addAll(setOperations.members("nickname:uid:*"+nickname+"*"));
                // }
            break;
            case ALL:
                sb.append("all");
                keySet.add("uids");
            break;
            //잘못 들어올 경우 리턴 값 없음.
            default:
                return null;
        }
        System.out.println("3차 완료");
        // video filter와 검색 결과를 담은 key Set이다.
        keySet.add(filterString);
        String searchString = sb.toString();
        sb.setLength(0);
        // 검색 결과 uid를 담는 Set이다. -> 교집합 통해 uid집합 만들기
        System.out.println("keySetSize>>>"+keySet.size());
        // for (String string : keySet) {
        //     System.out.println("keySet>>>>>"+string+"<<<");
        //     Set<Object> ts = setOperations.members(string);
        //     // System.out.println(setOperations.members(string).size());
        //     // System.out.println(setOperations.members(string).toString());
        //     for(Object obj : ts){
        //         System.out.println(obj);
        //     }
        // }
        Set<String> resultSet = setOperations.intersect(keySet);
        SearchPortfolioJoinBookmark tempResult;

        for(String uid : resultSet){
            System.out.println(uid);
        }
        for (String uid : resultSet) {
            tempResult = objectMapper.convertValue(hashOperations.entries("uid:"+uid), SearchPortfolioJoinBookmark.class);
            // tempResult = (SearchPortfolioJoinBookmark)hashOperations.entries(uid);
            // resultList.add(new SearchPortfolio(tempResult, setOperations.members(tempResult.getTagKey())));
            System.out.println(tempResult);
            if(tempResult.getTagKey() == null){
                setResultOperations.add(filterString+":"+searchString, new SearchPortfolio(tempResult, Collections.emptySet()));
            }
            else{
                setResultOperations.add(filterString+":"+searchString, new SearchPortfolio(tempResult, setOperations.members(tempResult.getTagKey())));
            }
        }
        System.out.println("4차 완료");
        //tagKey로 tagList로 바꾸어야한다.
        
        //검색 어도 key로 추가해 넣어야한다.
        // hashOperations.put(filterString+searchString, hashKey, value);
        resultList.addAll(setResultOperations.members(filterString+":"+searchString));

        System.out.println("5차 완료");
        // videoUidSet에 중복되는 Uid가 저장된다.
        // videoUidSet.retainAll(typeUidSet);

        // setOperations
        // 정렬 -> 가격 낮은, 가격 높은, 이름순, 평점순
        // SortQuery<String> sortQuery = SortQueryBuilder.sort("key").by("hash*->nickname").build();
        // 정렬 보류



        // 검색될 uid 집합
        // for (String uid : videoUidSet) {
            
        // }
        System.out.println("디비 캐시로 저장한 뒤 가져옴");
        return resultList;
    }

    // @Override
    // public Set<SearchPortfolio> getListIfExists(String searchKey) {

    // }

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

    @Override
    public long deleteAll(){
        Set<String> allKeys = this.hashKeys("*");
        return redisOperations.delete(allKeys);
    }
    
}