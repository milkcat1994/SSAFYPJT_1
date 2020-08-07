package com.web.editor.controller.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.editor.model.dto.user.schedule.HolidayDate;
import com.web.editor.model.service.user.schedule.HolidayDateService;

import io.swagger.annotations.ApiOperation;

//http://localhost:8080/swagger-ui.html

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

	@Autowired
	private HolidayDateService holidayDateService;

	// 편집자 회원의 휴일 조회
	@ApiOperation(value = "편집자의 개인 일정 리스트")
	@GetMapping("/holiday/{uid}")
	public Object progressReq(@PathVariable int uid) throws UnsupportedEncodingException {
		List<HolidayDate> list = holidayDateService.holidayDates(uid);

		if (!list.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("not exist", HttpStatus.NO_CONTENT);
		}
	}

	
}
