package com.web.editor.controller.user;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.SearchPageBean;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.user.SearchService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	SearchService service;
	
	@GetMapping("/listAll")
	public Object searchAll(Model model, HttpServletRequest req) {
		ResponseEntity response = null;
		final BasicResponse result = new BasicResponse();
		
		return response;
	}
	
}