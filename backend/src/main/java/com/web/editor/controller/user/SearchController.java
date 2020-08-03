package com.web.editor.controller.user;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.SearchPageBean;
import com.web.editor.model.service.user.SearchService;

@Controller
public class SearchController {

	@Autowired
	SearchService service;
	
	@GetMapping("/list")
	public String searchAll(Model model, HttpServletRequest req) {
		SearchPageBean bean = new SearchPageBean();
		
//나중에 태그 검색		
//		String[] cbs = req.getParameterValues("cb");
//		List<Integer> searchType = new ArrayList<>();
//		if (cbs != null) {
//			for (String cb: cbs) {
//				if ("".equals(cb)) {
//					searchType.add(1);
//				} else if ("".equals(cb)) {
//					searchType.add(2);
//				} else if ("".equals(cb)) {
//					searchType.add(3);
//				} else if ("".equals(cb)) {
//					searchType.add(4);
//				}
//			}
//		}
//		if (searchType.isEmpty()) {
//			searchType.add(1);
//			searchType.add(2);
//			searchType.add(3);
//			searchType.add(4);
//		}
//		bean.setSearchType(searchType);

		
		// 드롭다운
		String key = req.getParameter("key");
		key = key == null ? "" : key;
		String value = req.getParameter("word");
		value = value == null ? "" : value;
		if ("nickname".equals(key)) {
			bean.setNickname(value);
		} else if ("payMin".equals(key)) {
			bean.setPayMin(value);
		}
		
		String page = req.getParameter("pg");
		int currentPage = page == null ? 1 : Integer.parseInt(page);
		String spp = req.getParameter("spp");
		int sizePerPage = spp == null ? 10 : Integer.parseInt(spp);//sizePerPage
		
		List<Portfolio> PortfolioList = service.searchAll(currentPage, sizePerPage, bean);
		model.addAttribute("list", PortfolioList);
		return "portfolio/list";
	}
	
	
	@GetMapping("/read")
	public String search(Model model, HttpServletRequest req, HttpSession session) {
		int no = Integer.parseInt(req.getParameter("no"));
		Portfolio portfolio = service.search(no);
		model.addAttribute("portfolio", portfolio);
		return "portfolio/read";
	}
	
	
}
