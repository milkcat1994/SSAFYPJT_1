package com.web.editor.model.service.user.recommend;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.user.recommend.EditorDto;
import com.web.editor.model.mapper.user.RecommendMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    RecommendMapper recommendMapper;

    @Override
    public List<EditorDto> searchAllEditor() {        
        try {
            return recommendMapper.searchAllEditor();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EditorDto> searchBookEditorByUid(String uid) {
        try {
            return recommendMapper.searchBookEditorByUid(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
}