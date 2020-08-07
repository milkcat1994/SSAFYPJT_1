package com.web.editor.controller.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.editor.model.dto.request.RequestDateDto;
import com.web.editor.model.service.request.RequestDateService;

import io.swagger.annotations.ApiOperation;

//http://localhost:8080/swagger-ui.html

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/request/date")
public class RequestDateController {

	@Autowired
	private RequestDateService requestDateService;

	// 요청 리스트
	// 요청자(일반회원)의 리스트
	@ApiOperation(value = "요청자(일반회원)의 진행중 일정 리스트")
	@GetMapping("/req/{nickname}")
	public Object progressReq(@PathVariable String nickname) throws UnsupportedEncodingException {
		List<RequestDateDto> requestList = requestDateService.progressReq(decodeURL(nickname));

		if (!requestList.isEmpty()) {
			return new ResponseEntity<>(requestList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("not exist", HttpStatus.NO_CONTENT);
		}
	}

	// 요청받는사람(편집자)의 리스트
	@ApiOperation(value = "편집자의 진행중 일정 리스트")
	@GetMapping("/res/{nickname}")
	public Object progressRes(@PathVariable String nickname) throws UnsupportedEncodingException {
		
		List<RequestDateDto> responseList = requestDateService.progressRes(decodeURL(nickname));
		
		if (!responseList.isEmpty()) {
			return new ResponseEntity<>(responseList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("not exist", HttpStatus.NO_CONTENT);
		}
	}

	private String decodeURL(String target) throws UnsupportedEncodingException {
		return URLDecoder.decode(target, StandardCharsets.UTF_8.toString());
	}
	
}
