package com.web.editor.controller.user;

import java.util.List;

import com.web.editor.model.dto.user.PortfolioSchedule;
import com.web.editor.model.dto.user.PortfolioScheduleSaveRequest;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.user.PortfolioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/portfolio/schedule")
public class PortfolioScheduleController {
    @Autowired
    PortfolioService portfolioService;
    
    
    @GetMapping("/{uid}")
    @ApiOperation(value="해당 포트폴리오의 스케쥴 조회")
    public Object findScheduleByUid(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        List<PortfolioSchedule> schedule = portfolioService.findScheduleByUid(uid);

        //포트폴리오의 태그가 있을 경우
        if(schedule != null){
                result.status = true;
                result.data = "success";
                result.object = schedule;
                response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }
    
    // 일정등록
    @PostMapping("/{uid}")
    @ApiOperation(value="일정 등록")
    public Object scheduleSave(@PathVariable String uid, @RequestBody PortfolioScheduleSaveRequest scheduleSaveRequest){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        int res = portfolioService.scheduleSave(scheduleSaveRequest);

        // 저장에 성공
        if(res != -1){
            result.status = true;
            result.data = "success";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }
}