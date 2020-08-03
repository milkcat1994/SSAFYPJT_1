package com.web.editor.controller.user;

import java.util.List;

import com.web.editor.model.dto.user.PortfolioReview;
import com.web.editor.model.dto.user.PortfolioReviewSaveRequest;
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
@RequestMapping("/portfolio/review")
public class PortfolioReviewController {
    @Autowired
    PortfolioService portfolioService;
    
    
    @GetMapping("/{uid}")
    @ApiOperation(value="해당 포트폴리오의 리뷰 조회")
    public Object findReviewByUid(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        List<PortfolioReview> review = portfolioService.findReviewByUid(uid);

        //포트폴리오의 태그가 있을 경우
        if(review != null){
                result.status = true;
                result.data = "success";
                result.object = review;
                response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }
    
    // 리뷰등록
    @PostMapping("/{uid}")
    @ApiOperation(value="리뷰 등록")
    public Object scheduleSave(@PathVariable String uid, @RequestBody PortfolioReviewSaveRequest portfolioReviewSaveRequest){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        int res = portfolioService.reviewSave(portfolioReviewSaveRequest);

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