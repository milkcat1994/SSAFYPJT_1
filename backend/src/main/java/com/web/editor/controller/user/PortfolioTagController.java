package com.web.editor.controller.user;

import java.util.List;

import com.web.editor.model.dto.user.PortfolioTag;
import com.web.editor.model.dto.user.PortfolioTagRequest;
import com.web.editor.model.dto.user.PortfolioTagSaveRequest;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.user.PortfolioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/portfolio/tag")
public class PortfolioTagController {
    @Autowired
    PortfolioService portfolioService;
    
    
    @GetMapping("/{uid}")
    @ApiOperation(value="해당 포트폴리오의 태그 조회")
    public Object findTagByUid(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        List<PortfolioTag> portfolioTag = portfolioService.findTagByUid(uid);

        //포트폴리오의 태그가 있을 경우
        if(!portfolioTag.isEmpty()){
                result.status = true;
                result.data = "success";
                result.object = portfolioTag;
                response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }
    
    // 태그등록
    @PostMapping("/{uid}")
    @ApiOperation(value="태그 등록")
    public Object scheduleSave(@PathVariable String uid, @RequestBody PortfolioTagRequest portfolioTagRequest){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        int res = 0;
        for(int i=0; i < portfolioTagRequest.getTagName().size(); i++){
        res = portfolioService.tagSave(new PortfolioTagSaveRequest(Integer.parseInt(uid), portfolioTagRequest.getTagName().get(i)));

            // 저장에 성공
            if(res != -1){
                result.status = true;
                result.data = "success";
            } else {
                result.status = false;
                result.data = "fail";
                response = new ResponseEntity<>(result, HttpStatus.OK);
                return response;
            }
        }

        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    // 태그 수정
    @PutMapping("/{uid}")
    @ApiOperation(value="태그 수정")
    public Object updateTags(@PathVariable String uid, @RequestBody PortfolioTagRequest portfolioTagRequest){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        
        int res = portfolioService.deleteTags(uid);
        if(res < 0){
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
            return response;
        }

        for(int i=0; i < portfolioTagRequest.getTagName().size(); i++){
        res = portfolioService.tagSave(new PortfolioTagSaveRequest(Integer.parseInt(uid), portfolioTagRequest.getTagName().get(i)));

            // 저장에 성공
            if(res != -1){
                result.status = true;
                result.data = "success";
            } else {
                result.status = false;
                result.data = "fail";
                response = new ResponseEntity<>(result, HttpStatus.OK);
                return response;
            }
        }

        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }
}