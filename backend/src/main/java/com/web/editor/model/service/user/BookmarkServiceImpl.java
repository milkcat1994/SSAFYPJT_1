package com.web.editor.model.service.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.web.editor.model.dto.user.bookmark.Bookmark;
import com.web.editor.model.dto.user.bookmark.BookmarkDeleteRequest;
import com.web.editor.model.dto.user.bookmark.BookmarkInfo;
import com.web.editor.model.dto.user.bookmark.BookmarkSaveRequest;
import com.web.editor.model.mapper.user.BookmarkMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    @Autowired
    BookmarkMapper bookmarkMapper;

    @Override
    public List<Bookmark> findBookmarkByUid(String uid) {
        List<Bookmark> bookmarks = null;
        try {
            bookmarks = bookmarkMapper.findBookmarkByUid(uid);
            // 찾지 못한 경우
            if (bookmarks == null) {
                bookmarks = new ArrayList<>();
            }
            return bookmarks;
        } catch (SQLException e) {
            e.printStackTrace();
            return bookmarks;
        }
    }

    @Override
    public int bookmarkSave(BookmarkSaveRequest bookmarkSaveRequest) {
        try {
            int res = bookmarkMapper.bookmarkSave(bookmarkSaveRequest);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteBookmarkByUid(BookmarkDeleteRequest bookmarkDeleteRequest) {
        try {
            int res = bookmarkMapper.deleteBookmarkByUid(bookmarkDeleteRequest);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<BookmarkInfo> cntBookmarkByUid(String muid) {
        List<BookmarkInfo> bookmarks = null;
        try {
            // int res = 0;
            bookmarks = bookmarkMapper.cntBookmarkByUid(muid);
            // res = bookmarks.size();
            // 찾지 못한 경우
            if (bookmarks.isEmpty()) {
                // res = 0;
                bookmarks = new ArrayList<>();
            }
            return bookmarks;
        } catch (SQLException e) {
            e.printStackTrace();
            return bookmarks;
        }
    }
    
}