package com.web.editor.controller.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

import com.web.editor.model.dto.request.SearchAverageScore;
import com.web.editor.model.dto.request.SearchRequestVideoInfo;
import com.web.editor.model.dto.user.search.SearchPortfolio;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinBookmark;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinVideo;
import com.web.editor.model.dto.user.search.SearchRequest;
import com.web.editor.model.dto.user.search.SearchTag;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.request.RequestService;
import com.web.editor.model.service.user.search.SearchService;
import com.web.editor.model.service.user.search.redis.SearchRedisService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/search")
public class SearchController {

	// @Autowired
	// SearchService service;

	// @Autowired
	// PortfolioService portfolioService;
	
	// @Autowired
    // BookmarkService bookmarkService;
	
	// Redis 이용 위한 service
	@Autowired
	SearchRedisService searchRedisService;

	@Autowired
	SearchService searchService;

	@Autowired
	RequestService requestService;

	@PostMapping("")
    @ApiOperation(value="검색 수행")
	public Object search(@RequestBody SearchRequest searchRequest){
		//Redis에서 해당 검색 수행하기
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		List<SearchPortfolio> searchList = new ArrayList<>();
		
		//만약 Redis가 연결되지 않는다면 DataBase에서 직접 값을 뽑아와야한다.
		try {
			searchList = searchRedisService.getListByFilter(searchRequest);
			// System.out.println(searchList);
		} catch (RedisConnectionFailureException e) {
			System.out.println("Redis가 연결 되지 않습니다.");
			// Redis 연결오류 났을 경우
			List<SearchPortfolioJoinBookmark> searchPortfolioJoinBookmarks = searchService.joinBookmarks();
			List<SearchPortfolioJoinVideo> searchPortfolioJoinVideos = searchService.joinVideos();
			List<SearchAverageScore> searchAverageScores = requestService.joinScores();

			//위의 정보들로 유저 정보 만들어내기
			Map<Integer, SearchPortfolioJoinBookmark> initMap = new HashMap<>();
			Map<String, Integer> uidMap = new HashMap<>();
			Set<Integer> allUidSet = new HashSet<>();
			Map<Integer, SearchPortfolioJoinVideo> videoMap = new HashMap<>();
			Map<String, SearchAverageScore> averageScoreMap = new HashMap<>();
			
			for (SearchPortfolioJoinBookmark searchPortfolioJoinBookmark : searchPortfolioJoinBookmarks) {
				allUidSet.add(searchPortfolioJoinBookmark.getUid());
				initMap.put(searchPortfolioJoinBookmark.getUid(), searchPortfolioJoinBookmark);
				uidMap.put(searchPortfolioJoinBookmark.getNickname(), searchPortfolioJoinBookmark.getUid());
				// target = bookmarkMap.get(searchPortfolioJoinBookmark.getUid());
				// target.setUrl(url);
			}
			
			for (SearchPortfolioJoinVideo searchPortfolioJoinVideo : searchPortfolioJoinVideos) {
				videoMap.put(searchPortfolioJoinVideo.getUid(), new SearchPortfolioJoinVideo(searchPortfolioJoinVideo));
			}
			
			for (SearchAverageScore searchAverageScore : searchAverageScores) {
				averageScoreMap.put(searchAverageScore.getNickname(), new SearchAverageScore(searchAverageScore));
			}
			
			// videoStyle, videoType, videoSkill 은 Set으로 진행할것
			// 모든 nickname과 video정보
			List<SearchRequestVideoInfo> searchRequestVideoInfos = requestService.searchRequestVideoInfo();

			// video => uid Set
			Map<String, Set<Integer>> videoFilterMap = new HashMap<>();

			// 검색 요청으로 들어온 video 필터 정보
			Set<String> videoFilterSet = new HashSet<>();
			videoFilterSet.addAll(searchRequest.getVideoTypes());
			videoFilterSet.addAll(searchRequest.getVideoStyles());
			videoFilterSet.addAll(searchRequest.getVideoSkills());

			StringTokenizer st;
			String vidoeSkill, nickname;
			for(SearchRequestVideoInfo searchRequestVideoInfo : searchRequestVideoInfos){
				nickname = searchRequestVideoInfo.getNickname();
				// 검색 요청에 해당 videoType이 들어있다면
				if(videoFilterSet.contains(searchRequestVideoInfo.getVideoType())) {
					// 해당 videoType이 key로 만들어져 있다면 해당 Set에 uid를 더해준다.
					if(videoFilterMap.containsKey(searchRequestVideoInfo.getVideoType())){
						videoFilterMap.get(searchRequestVideoInfo.getVideoType()).add(uidMap.get(nickname));
					}
					else{
						videoFilterMap.put(searchRequestVideoInfo.getVideoType(), new HashSet<Integer>());
						videoFilterMap.get(searchRequestVideoInfo.getVideoType()).add(uidMap.get(nickname));
					}
				}
				// 검색 요청에 해당 videoStyle이 들어있다면
				if(videoFilterSet.contains(searchRequestVideoInfo.getVideoStyle())) {
					// 해당 videoStyle이 key로 만들어져 있다면 해당 Set에 uid를 더해준다.
					if(videoFilterMap.containsKey(searchRequestVideoInfo.getVideoStyle())){
						videoFilterMap.get(searchRequestVideoInfo.getVideoStyle()).add(uidMap.get(nickname));
					}
					else{
						videoFilterMap.put(searchRequestVideoInfo.getVideoStyle(), new HashSet<Integer>());
						videoFilterMap.get(searchRequestVideoInfo.getVideoStyle()).add(uidMap.get(nickname));
					}
				}

				st = new StringTokenizer(searchRequestVideoInfo.getVideoSkill(), ",");
				while(st.hasMoreTokens()){
					vidoeSkill = st.nextToken().trim();
					if(videoFilterSet.contains(vidoeSkill)) {
						if(videoFilterMap.containsKey(vidoeSkill)){
							videoFilterMap.get(vidoeSkill).add(uidMap.get(nickname));
						}
						else{
							videoFilterMap.put(vidoeSkill, new HashSet<Integer>());
							videoFilterMap.get(vidoeSkill).add(uidMap.get(nickname));
						}
					}
				}
			}

			// searchRedisService.portfolioTagSave(searchService.searchPortfolioTag());
			// Tag 
			List<SearchTag> searchTags = searchService.searchPortfolioTag();
			Map<String, Set<Integer>> tagUidMap = new HashMap<>();
			Map<Integer, Set<String>> uidTagMap = new HashMap<>();
			String tagName;
			Integer tagUid;
			for (SearchTag searchTag : searchTags) {
				if(searchTag == null) continue;
				tagName = searchTag.getTagName();
				tagUid = searchTag.getUid();
				if(tagUidMap.containsKey(tagName)){
					tagUidMap.get(tagName).add(tagUid);
				}
				else{
					tagUidMap.put(tagName, new HashSet<Integer>());
					tagUidMap.get(tagName).add(tagUid);
				}

				// uid가 가지는 tag저장
				if(uidTagMap.containsKey(tagName)){
					uidTagMap.get(tagUid).add(tagName);
				}
				else{
					uidTagMap.put(tagUid, new HashSet<String>());
					uidTagMap.get(tagUid).add(tagName);
				}
			}


			// searchRequest에서 받은 video skill, type, style 를 통해 uid Set을 union한다.

			// 필터에 대한 uid
			Set<Integer> filterResulltSet = new HashSet<>();
			// 필터에 대한 합집합
			for (String videoFilter : videoFilterSet) {
				filterResulltSet.addAll(videoFilterMap.get(videoFilter));
			}
			//모든 uid 필요
			if(videoFilterSet.isEmpty()){
				filterResulltSet.addAll(allUidSet);
			}

			// tag검색인지 nickname 검색인지
			// uidMap 이용
			// 검색어 uid
			Set<Integer> SearchResulltSet = new HashSet<>();
			switch (searchRequest.getSearchType()) {
				case "TAG":
					if(searchRequest.getSearchTags().size() >= 1 && searchRequest.getSearchTags().get(0).trim().equals("")){
						SearchResulltSet.addAll(allUidSet);
					}
					else{
						SearchResulltSet.addAll(allUidSet);
						for(String tag : searchRequest.getSearchTags()){
							// tag검색은 교집합으로 검색
							SearchResulltSet.retainAll(tagUidMap.get(tag));
						}
					}
					break;
				case "NICKNAME":
					Set<String> set = uidMap.keySet();
					if(searchRequest.getSearchText().trim().equals("")){
						SearchResulltSet.addAll(allUidSet);
					}
					else{
						for (String nick : set) {
							// 해당 검색어를 이름의 일부로 가지고 있다면 결과에 포함 시킬 예정이다.
							if(nick.contains(searchRequest.getSearchText())){
								SearchResulltSet.add(uidMap.get(nick));
							}
						}
					}
					break;
				case "ALL":
					SearchResulltSet.addAll(allUidSet);
					break;
				default:
					return null;
			}

			SearchResulltSet.retainAll(filterResulltSet);

			// 결과 uid 돌면서 리스트로 띄워줄 방식으로 만든다.
			Set<SearchPortfolio> resultSet = new HashSet<>();
			SearchPortfolio tempResult;
			for (Integer uid : SearchResulltSet) {
				// 해당 uid가 가지는 tag가 있는지
				if(!uidTagMap.containsKey(uid)){
					resultSet.add(new SearchPortfolio(initMap.get(uid), Collections.emptySet()));
				}
				else{
					resultSet.add(new SearchPortfolio(initMap.get(uid), uidTagMap.get(uid)));
				}
			}

			searchList.addAll(resultSet);

			switch (searchRequest.getSortType()) {
				case "PRICE_ASC":
					Collections.sort(searchList, (a, b) -> a.getPayMin() - b.getPayMin());
					break;
				case "PRICE_DESC":
					Collections.sort(searchList, (a, b) -> b.getPayMin() - a.getPayMin());
					break;
				case "NICKNAME_ASC":
				Collections.sort(searchList, (a, b) -> a.getNickname().compareTo(b.getNickname()));
					break;
				case "SCORE_DESC":
				default:
				Collections.sort(searchList, (a, b) -> Float.compare(b.getAvgScore(), a.getAvgScore()));
					break;
			}
		}

		result.status = true;
		result.data = "success";
		result.object = searchList;
		
		return result;
	}



	// @GetMapping("/listAll")
	// public Object searchAll() {
	// 	ResponseEntity response = null;
	// 	final BasicResponse result = new BasicResponse();
	// 	List<PortfolioList> searhList = service.searchAll();
		
	// 	if(!searhList.isEmpty()){
	// 		List<PortfolioTag> portfolioTags = new ArrayList<>();
	// 		List<PortfolioVideo> urls = new ArrayList<>();
	// 		List<BookmarkInfo> bookmarks = new ArrayList<>();
	// 		for (int i = 0; i < searhList.size(); i++) {
	// 			String uid = String.valueOf(searhList.get(i).getUid());
	// 			portfolioTags = portfolioService.findTagByUid(uid);
	// 			urls = portfolioService.findVideoByUid(uid);
	// 			bookmarks = bookmarkService.cntBookmarkByUid(uid);

	// 			searhList.get(i).setBookmarkCount(bookmarks.size());
	// 			searhList.get(i).setURLs(urls.stream().map(u -> u.getUrl()).collect(Collectors.toList()));
	// 			searhList.get(i).setTags(portfolioTags.stream().map(e -> e.getTagName()).collect(Collectors.toCollection(ArrayList::new)));
	// 		}
	// 		result.status = true;
    //         result.data = "success";
    //         result.object = searhList;
	// 	} else {
	// 		result.status = false;
	// 		result.data = "fail";
	// 	}
	// 	response = new ResponseEntity<>(result, HttpStatus.OK);
	// 	return response;
	// }
	
}
