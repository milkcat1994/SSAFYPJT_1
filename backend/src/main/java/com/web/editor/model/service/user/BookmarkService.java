package com.web.editor.model.service.user;

import java.util.List;

import com.web.editor.model.dto.user.bookmark.Bookmark;
import com.web.editor.model.dto.user.bookmark.BookmarkDeleteRequest;
import com.web.editor.model.dto.user.bookmark.BookmarkInfo;
import com.web.editor.model.dto.user.bookmark.BookmarkSaveRequest;

public interface BookmarkService {
    
    public List<Bookmark> findBookmarkByUid(String uid);

    public List<BookmarkInfo> cntBookmarkByUid(String muid);
    
    public int bookmarkSave(BookmarkSaveRequest bookmarkSaveRequest);
    
	public int deleteBookmarkByUid(BookmarkDeleteRequest bookmarkDeleteRequest);
}