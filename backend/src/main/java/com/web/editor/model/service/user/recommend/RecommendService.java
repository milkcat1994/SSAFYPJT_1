package com.web.editor.model.service.user.recommend;

import java.util.List;

import com.web.editor.model.dto.user.recommend.EditorDto;

public interface RecommendService {
    
    // 전체 편집자 검색
    public List<EditorDto> searchAllEditor ();
    
    // 북마크한 편집자 검색
    public List<EditorDto> searchBookEditorByUid (String uid);
}