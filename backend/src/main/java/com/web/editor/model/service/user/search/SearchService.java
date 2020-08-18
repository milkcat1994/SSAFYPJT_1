package com.web.editor.model.service.user.search;

import java.util.List;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.PortfolioList;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinBookmark;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinVideo;
import com.web.editor.model.dto.user.search.SearchTag;

public interface SearchService {

	public List<PortfolioList> searchAll();
	
	public Portfolio search(int no);
	
	public List<SearchPortfolioJoinBookmark> joinBookmarks();
	
	// 포트폴리오 - 영상
	// uid, mainUrl
	public List<SearchPortfolioJoinVideo> joinVideos();

	// 포트폴리오에 등록된 tag
	public List<SearchTag> searchPortfolioTag();
}
