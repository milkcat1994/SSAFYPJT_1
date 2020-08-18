package com.web.editor.controller.user;

import java.util.StringTokenizer;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.PortfolioInfo;
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
@RequestMapping("/portfolio/portfolio")
public class PortfolioController {
    @Autowired
    PortfolioService portfolioService;
    
    @GetMapping("/{uid}")
    @ApiOperation(value="포트폴리오 조회")
    public Object findPortfolioByUid(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        Portfolio portfolio = portfolioService.findPortfolioByUid(uid);

        //포트폴리오가 있을 경우
        if(portfolio != null){
            try{
                portfolio.setUid(Integer.parseInt(uid));
                result.status = true;
                result.data = "success";
                result.object = portfolio;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }
            catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                result.status = false;
                result.data = "uid_need_int";
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("/{uid}")
    @ApiOperation(value="첫 가입시 포트폴리오 초기 등록")
    public Object portfolioInitRegister(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        //해당 user 포트폴리오 존재하는지 확인 이후 진행
        Portfolio portfolio = portfolioService.findPortfolioByUid(uid);
        // uid중복
        String message = "uid";

        if(portfolio != null){
            //uid가 양수라면 이미 있는 것.
            if(portfolio.getUid() > 0){
                result.status = false;
            }
            //uid가 양수가 아니라면 없는 것.
            else{
                int res = portfolioService.portfolioInitSave(uid);
                // 성공
                if(res > 0){
                    result.status = true;
                    message = "success";
                }
                // 등록 실패
                else{
                    result.status = false;
                    message = "fail";
                }
            }
        }
        //SQL 예외 발생으로 인한 null반환
        else{
            result.status = false;
            message = "fail";
        }
        result.data = message;
        
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @PutMapping("/{uid}")
    @ApiOperation(value="포트폴리오 수정")
    public Object updatePortfolio(@PathVariable String uid, @RequestBody PortfolioInfo portfolioInfo){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        if(portfolioInfo.getSkill().length() > 0){
            StringTokenizer st = new StringTokenizer(portfolioInfo.getSkill(), ",");
            String skills = "";
            while(st.hasMoreTokens()){
                String skill = st.nextToken();
                if(skill.equals("color")){
                    skills += ",색/밝기 조정";
                } else if(skill.equals("audio")){
                    skills += ",음향";
                } else if(skill.equals("motion")){
                    skills += ",모션그래픽";
                } else if(skill.equals("caption")){
                    skills += ",자막";
                } else if(skill.equals("intro")){
                    skills += ",인트로";
                } else if(skill.equals("outro")){
                    skills += ",아웃트로";
                }
            }
            int len = skills.length();
            skills = skills.substring(1,len);
            portfolioInfo.setSkill(skills);
        }
        
        int res = portfolioService.portfolioUpdate(portfolioInfo);
        if(res != -1){
            result.status = true;
            result.data = "success";
        } else {
            result.status = false;
            result.data = "fail";
        }
        
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }
}