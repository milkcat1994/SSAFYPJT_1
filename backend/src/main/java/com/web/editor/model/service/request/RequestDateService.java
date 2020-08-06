package com.web.editor.model.service.request;

import java.util.List;

import com.web.editor.model.dto.request.RequestDateDto;

public interface RequestDateService {

    // 일반회원 진행 일정 조회
    public List<RequestDateDto> progressReq(String nickname);
    
    // 편집자 회원 진행 일정 조회
    public List<RequestDateDto> progressRes(String nickname);
    
}
