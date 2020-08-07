package com.web.editor.model.service.user;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.SearchPageBean;
import com.web.editor.model.mapper.user.SearchMapper;

@Service
public class SearchServiceImpl implements SearchService{
	
	@Autowired
	private SearchMapper searchMapper;

	public List<Portfolio> searchAll(int currentPage, int sizePerPage, SearchPageBean bean){
		List<Portfolio> list = new ArrayList<>();
		try {
			currentPage = (currentPage-1) * sizePerPage;
			Map<String, Object> map = new HashMap<>();
			map.put("nickname", bean.getNickname());
			map.put("payMin", bean.getPayMin());
			map.put("currentPage", currentPage);
			map.put("sizePerPage", sizePerPage);
			return searchMapper.searchAll(map);
		} catch (Exception e) {
			// throw new SearchException("포트폴리오 조회 중 오류 발생");			
		}
		return list;
	}
	
	public Portfolio search(int uid) {
		Portfolio portfolio = new Portfolio();
		try {
			portfolio = searchMapper.search(uid);
			if (portfolio == null) {
				// throw new SearchException(String.format("%d번에 해당하는 포르폴리오 정보가 존재하지 않습니다.", uid));
				return portfolio;
			}
			return portfolio;
		} catch (Exception e) {
			// throw new SearchException("포트폴리오 조회 중 오류 발생");	
		}
		return portfolio;
	}
		
}
