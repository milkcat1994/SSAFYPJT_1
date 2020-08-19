package com.web.editor.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.web.editor.model.dto.user.PortfolioTag;
import com.web.editor.model.dto.user.bookmark.Bookmark;
import com.web.editor.model.dto.user.bookmark.BookmarkDeleteRequest;
import com.web.editor.model.dto.user.bookmark.BookmarkInfo;
import com.web.editor.model.dto.user.bookmark.BookmarkSaveRequest;
import com.web.editor.model.response.BasicResponse;
import com.web.editor.model.service.user.BookmarkService;
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
@RequestMapping("/bookmark")
public class BookmarkController {
    
    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    PortfolioService portfolioService;

    @GetMapping("/{uid}")
    @ApiOperation(value="북마크 조회")
    public Object findPortfolioByUid(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        List<Bookmark> bookmarks = bookmarkService.findBookmarkByUid(uid);

        //포트폴리오가 있을 경우
        if(bookmarks != null){
            // bookmarks 를 돌면서 해당 bookmarks muid에 태그 정보 반환 하여 추가시켜 주기
            List<PortfolioTag> portfolioTags = new ArrayList<>();

            for (Bookmark bookmark : bookmarks) {
                portfolioTags = portfolioService.findTagByUid(String.valueOf(bookmark.getMuid()));
                bookmark.setTags(portfolioTags.stream().map(e -> e.getTagName()).collect(Collectors.toCollection(ArrayList::new)));
            }
            // List<String> ids = persons.stream().map(e -> e.id).collect(Collectors.toCollection(ArrayList::new));
            result.status = true;
            result.data = "success";
            result.object = bookmarks;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/cnt/{uid}")
    @ApiOperation(value = "편집자 북마크한 인원 수와 uid 가져오기")
    public Object countBookmark(@PathVariable String uid){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        List<BookmarkInfo> bookmarks = bookmarkService.cntBookmarkByUid(uid);
        result.status = true;
        result.data = "success";
        result.object = bookmarks;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }


    @PostMapping("")
    @ApiOperation(value="북마크 추가")
    public Object portfolioInitRegister(@RequestBody BookmarkSaveRequest bookmarkSaveRequest){
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        //해당 user 포트폴리오 존재하는지 확인 이후 진행
        int res = bookmarkService.bookmarkSave(bookmarkSaveRequest);

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
    
    @PostMapping("/delete")
    @ApiOperation(value = "북마크 삭제")
    public Object deleteBookmarkByUid(@RequestBody BookmarkDeleteRequest bookmarkDeleteRequest) {
        int res = bookmarkService.deleteBookmarkByUid(bookmarkDeleteRequest);
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();

        if (res > 0) {
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