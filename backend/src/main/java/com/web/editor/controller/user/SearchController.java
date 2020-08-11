package com.web.editor.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.editor.model.dto.user.PortfolioList;
import com.web.editor.model.dto.user.PortfolioTag;
import com.web.editor.model.dto.user.PortfolioVideo;
import com.web.editor.model.dto.user.bookmark.BookmarkInfo;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.user.BookmarkService;
import com.web.editor.model.service.user.PortfolioService;
import com.web.editor.model.service.user.SearchService;

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
	
	@GetMapping("/listAll")
	public Object searchAll() {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		List<PortfolioList> searhList = service.searchAll();
		
		if(!searhList.isEmpty()){
			List<PortfolioTag> portfolioTags = new ArrayList<>();
			List<PortfolioVideo> urls = new ArrayList<>();
			List<BookmarkInfo> bookmarks = new ArrayList<>();
			for (int i = 0; i < searhList.size(); i++) {
				String uid = String.valueOf(searhList.get(i).getUid());
				portfolioTags = portfolioService.findTagByUid(uid);
				urls = portfolioService.findVideoByUid(uid);
				bookmarks = bookmarkService.cntBookmarkByUid(uid);

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
