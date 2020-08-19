package com.web.editor.controller.recommend;

import java.util.List;

import com.web.editor.model.dto.user.recommend.EditorDto;
import com.web.editor.model.service.user.recommend.RecommendService;
import com.web.editor.util.recommend.CfAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    RecommendService recommendService;
    
    // 북마크 기반 추천
	@ApiOperation(value = "사용자가 찜한 편집자 기반으로 추천")
	@PostMapping("/{uid}")
	public Object recommendByBookmark(@PathVariable String uid) {

        CfAlgorithm cfAlgorithm = new CfAlgorithm();

        List<EditorDto> editors = recommendService.searchAllEditor();  // 전체 에디터
        List<EditorDto> bookmarks = recommendService.searchBookEditorByUid(uid);   // 북마크한 에디터

        // 북마크가 없음
        if (bookmarks == null) return new ResponseEntity<>("bookmark is empty", HttpStatus.OK);
        if (bookmarks.size() <= 0) return new ResponseEntity<>("bookmark is empty", HttpStatus.OK);

        // 전체 북마크의 태그들
        EditorDto bookmark = new EditorDto();
        bookmark.setTag("");
        bookmark.setSkill("");
        bookmark.setUid("");
        for (EditorDto editor: bookmarks){
            bookmark.setUid(bookmark.getUid().concat("," + editor.getUid()));
            bookmark.setTag(bookmark.getTag().concat("," + editor.getTag()));
            bookmark.setSkill(bookmark.getSkill().concat("," + editor.getSkill()));
        }
        bookmark.setUid(bookmark.getUid().substring(1));
        bookmark.setTag(bookmark.getTag().substring(1));
        bookmark.setSkill(bookmark.getSkill().substring(1));

        // 추천 편집자 리스트
        List<EditorDto> recommendList = cfAlgorithm.recommendByBookmark(bookmark, editors, uid);
        
        return new ResponseEntity<>(recommendList, HttpStatus.OK);
    }
}