package com.web.editor.model.mapper.user;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.user.bookmark.Bookmark;
import com.web.editor.model.dto.user.bookmark.BookmarkDeleteRequest;
import com.web.editor.model.dto.user.bookmark.BookmarkInfo;
import com.web.editor.model.dto.user.bookmark.BookmarkSaveRequest;
import com.web.editor.model.dto.user.bookmark.BookmarkUid;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class BookmarkMapper {
    @Autowired
    @Qualifier("firstSqlSessionTemplate")
    private SqlSession sqlSession;
    
    /**
     * 해당 사용자의 북마크 모두 선택
     * @param uid   북마크 팔로워(하는)
     * @return      북마크 팔로잉(당하는) List<muid> 반환
     * @throws SQLException
     */
    public List<Bookmark> findBookmarkByUid(String uid) throws SQLException{
        return sqlSession.selectList("findBookmarkByUid", uid);
    }

    public List<BookmarkInfo> cntBookmarkByUid(String muid) throws SQLException {
        return sqlSession.selectList("cntBookmarkByUid", Integer.parseInt(muid));
    }
    
    /**
     * 
     * @param bookmarkSaveRequest
     * uid: 북마크 팔로워(하는)
     * muid:  북마크 팔로잉(당하는)
     * @return      성공 1, 실패 0
     * @throws SQLException
     */
	public int bookmarkSave(BookmarkSaveRequest bookmarkSaveRequest) throws SQLException {
		return sqlSession.insert("bookmarkSave", bookmarkSaveRequest);
    }
    
    // uid이용 muid 삭제
	public int deleteBookmarkByUid(BookmarkDeleteRequest bookmarkDeleteRequest) throws SQLException {
		return sqlSession.delete("deleteBookmarkByUid", bookmarkDeleteRequest);
	}
}