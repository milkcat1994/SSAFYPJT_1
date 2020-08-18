package com.web.editor.model.mapper.user;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.user.recommend.EditorDto;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendMapper {
    @Qualifier("firstSqlSessionTemplate")

    @Autowired
    SqlSession sqlSession;

    public List<EditorDto> searchAllEditor () throws SQLException {
        return sqlSession.selectList("searchAllEditor");
    }

    public List<EditorDto> searchBookEditorByUid (String uid) throws SQLException {
        return sqlSession.selectList("searchBookEditorByUid", uid);
    }

}