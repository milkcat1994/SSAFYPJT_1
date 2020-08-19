package com.web.editor.controller.user;

import java.util.List;

import com.web.editor.model.dto.user.PortfolioVideo;
import com.web.editor.model.dto.user.PortfolioVideoDeleteRequest;
import com.web.editor.model.dto.user.PortfolioVideoRequest;
import com.web.editor.model.dto.user.PortfolioVideoSaveRequest;
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
@RequestMapping("/portfolio/video")
public class PortfolioVideoController {
    @Autowired
    PortfolioService portfolioService;
    
    
    @GetMapping("/{uid}")
    @ApiOperation(value="해당 포트폴리오의 영상 조회")
    public Object findVideoByUid(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        List<PortfolioVideo> video = portfolioService.findVideoByUid(uid);

        //포트폴리오의 영상이 있을 경우
        if(!video.isEmpty()){
            result.status = true;
            result.data = "success";
            result.object = video;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }
    
    // 영상등록
    @PostMapping("/{uid}")
    @ApiOperation(value="영상 등록")
    public Object videoSave(@PathVariable String uid, @RequestBody PortfolioVideoRequest portfolioVideoRequest){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        String URL = "https://www.youtube.com/embed/";
        List<String> urlList = portfolioVideoRequest.getUrl();
        int size = urlList.size();
        for(int i=0; i<size;i++){
            String[] temp = urlList.get(i).split("=");
            String url = URL + temp[1];
            urlList.set(i, url);
        }
        portfolioVideoRequest.setUrl(urlList);

        int res = -1;
        for(int i=0;i<size;i++){
            res = portfolioService.videoSave(new PortfolioVideoSaveRequest(Integer.parseInt(uid), portfolioVideoRequest.getUrl().get(i),
                                                                                portfolioVideoRequest.getMainFlag()));
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

    @PutMapping("/{uid}")
    @ApiOperation(value = "영상 삭제 및 재등록")
    public Object videoUpdate(@PathVariable String uid, @RequestBody PortfolioVideoRequest portfolioVideoRequest){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        int res = 0;
        
        // 모든 videos 삭제
        res = portfolioService.deleteVideos(new PortfolioVideoDeleteRequest(Integer.parseInt(uid), portfolioVideoRequest.getMainFlag()));
        if(res < 0){
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
            return response;
        }
        
        String URL = "https://www.youtube.com/embed/";
        String[] temp;

        List<String> urlList = portfolioVideoRequest.getUrl();
        int size = urlList.size();
        String url = "";
        for(int i=0; i<size;i++){
            temp = urlList.get(i).split("=");
            if(temp.length > 1){
                url = URL + temp[1];
            } else {
                url = urlList.get(i);
            }
            urlList.set(i, url);
        }
        portfolioVideoRequest.setUrl(urlList);
        
        for(int i=0;i<size;i++){
            res = portfolioService.videoSave(new PortfolioVideoSaveRequest(Integer.parseInt(uid), portfolioVideoRequest.getUrl().get(i),
                                                                                portfolioVideoRequest.getMainFlag()));
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