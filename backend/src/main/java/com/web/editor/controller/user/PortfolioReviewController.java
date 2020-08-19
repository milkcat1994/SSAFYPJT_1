package com.web.editor.controller.user;

import java.util.List;

import com.web.editor.model.dto.user.PortfolioReview;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.request.RequestService;
import com.web.editor.model.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/portfolio/review")
public class PortfolioReviewController {
    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;
    
    
    @GetMapping("/{uid}")
    @ApiOperation(value="해당 포트폴리오의 리뷰 조회")
    public Object findReviewByUid(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        String nickname;
        List<PortfolioReview> review;
        try{
            nickname = userService.findByUid(uid).getNickname();
            review = requestService.searchReviewList(nickname);
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
        }
        // 해당하는 uid를 가지는 user없음.
        catch(NullPointerException npe){
            npe.printStackTrace();
        }

        return response;
    }
}