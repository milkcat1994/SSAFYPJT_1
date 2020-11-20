package com.web.editor.model.service.user.search;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.PortfolioList;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinBookmark;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinVideo;
import com.web.editor.model.dto.user.search.SearchTag;
import com.web.editor.model.mapper.user.SearchMapper;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchMapper searchMapper;

	public List<PortfolioList> searchAll() {
		List<PortfolioList> list = new ArrayList<>();
		try {
			return searchMapper.searchAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Portfolio search(int uid) {
		Portfolio portfolio = new Portfolio();
		try {
			portfolio = searchMapper.search(uid);
			if (portfolio == null) {
				return portfolio;
			}
			return portfolio;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return portfolio;
	}

	@Override
	public List<SearchPortfolioJoinBookmark> joinBookmarks() {
		List<SearchPortfolioJoinBookmark> list = new ArrayList<>();
		try {
			return searchMapper.joinBookmarks();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<SearchPortfolioJoinVideo> joinVideos() {
		List<SearchPortfolioJoinVideo> list = new ArrayList<>();
		try {
			return searchMapper.joinVideos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<SearchTag> searchPortfolioTag() {
		List<SearchTag> list = new ArrayList<>();
		try {
			return searchMapper.searchPortfolioTag();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
		
}
