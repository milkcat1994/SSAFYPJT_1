package com.web.editor.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;

import com.web.editor.model.dto.user.PortfolioList;
import com.web.editor.model.dto.user.PortfolioTag;
import com.web.editor.model.dto.user.PortfolioVideo;
import com.web.editor.model.dto.user.bookmark.Bookmark;
import com.web.editor.model.dto.user.search.SearchPortfolio;
import com.web.editor.model.dto.user.search.SearchRequest;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.request.RequestService;
import com.web.editor.model.service.user.BookmarkService;
import com.web.editor.model.service.user.PortfolioService;
import com.web.editor.model.service.user.search.SearchService;
import com.web.editor.model.service.user.search.redis.SearchRedisService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	SearchService service;

	@Autowired
	PortfolioService portfolioService;
	
	@Autowired
    BookmarkService bookmarkService;
	
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
		List<SearchPortfolio> searchList = null;
		
		// 일단 여기에서 모든 처리 다해보기
		// 검색시 해당 검색 결과가 존재하는지, 존재 하지 않는다면 
		
		//만약 Redis가 연결되지 않는다면 DataBase에서 직접 값을 뽑아와야한다.


		// 주석의 경우 기초 작업으로 정해진 시간마다 실행 되어야한다.
		// long res = searchRedisService.deleteAll();
		// System.out.println("지워진수>>>>"+res);
		// // DB내용 Redis로 끌어올 필요 있음. 
		// searchRedisService.portfolioAndBookmarkSave(searchService.joinBookmarks());
		// searchRedisService.portfolioAndVideoSave(searchService.joinVideos());
		// searchRedisService.requestAndReviewSave(requestService.joinScores());
		// searchRedisService.makeUserInfo();
		// searchRedisService.searchRequestVideoInfoSave(requestService.searchRequestVideoInfo());
		// searchRedisService.portfolioTagSave(searchService.searchPortfolioTag());
		
		//key값을 Redis에서 먼저 찾아 값을 반환한다.
		// getListByFilter 함수 내에서 Redis에서 Key값을 먼저 찾고
		// key값이 없을 경우 새롭게 key를 만들고 값을 리턴한다.
		searchList = searchRedisService.getListByFilter(searchRequest);
		result.status = true;
		result.data = "success";
		result.object = searchList;
		
		return result;
	}



	@GetMapping("/listAll")
	public Object searchAll() {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		List<PortfolioList> searhList = service.searchAll();
		
		if(!searhList.isEmpty()){
			List<PortfolioTag> portfolioTags = new ArrayList<>();
			List<PortfolioVideo> urls = new ArrayList<>();
			List<Bookmark> bookmarks = new ArrayList<>();
			for (int i = 0; i < searhList.size(); i++) {
				String uid = String.valueOf(searhList.get(i).getUid());
				portfolioTags = portfolioService.findTagByUid(uid);
				urls = portfolioService.findVideoByUid(uid);
				bookmarks = bookmarkService.findBookmarkByUid(uid);

				searhList.get(i).setBookmarkCount(bookmarks.size());
				searhList.get(i).setURLs(urls.stream().map(u -> u.getUrl()).collect(Collectors.toList()));
				searhList.get(i).setTags(portfolioTags.stream().map(e -> e.getTagName()).collect(Collectors.toCollection(ArrayList::new)));
			}
			result.status = true;
            result.data = "success";
            result.object = searhList;
		} else {
			result.status = false;
			result.data = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}
	
}
