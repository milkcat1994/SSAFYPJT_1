package com.web.editor.model.service.request;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.request.RequestDateDto;
import com.web.editor.model.mapper.request.RequestDateMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestDateServiceImpl implements RequestDateService {
    
    @Autowired
    RequestDateMapper requestDateMapper;

    // 일반회원의 진행중 작업 일정 반환
    @Override
    public List<RequestDateDto> progressReq(String nickname) {
        try {
            return requestDateMapper.progressReq(nickname);
        } catch (SQLException e) {
            e.printStackTrace();   
            return null;    
        }
    }

    // 편집자의 진행중 작업 
    @Override
    public List<RequestDateDto> progressRes(String nickname) {
        try {
            return requestDateMapper.progressRes(nickname);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    

}
