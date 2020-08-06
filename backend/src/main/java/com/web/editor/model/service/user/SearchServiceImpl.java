package com.web.editor.model.service.user;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.editor.model.dto.user.PortfolioList;
import com.web.editor.model.mapper.user.SearchMapper;

@Service
public class SearchServiceImpl implements SearchService{
	
	@Autowired
	private SearchMapper searchMapper;

	public List<PortfolioList> searchAll(){
		List<PortfolioList> list = null;
		try {
			list = searchMapper.searchAll();
			if(list.isEmpty()){
				list = new ArrayList<>();
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}
	}
	
}
