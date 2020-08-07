package com.web.editor.model.mapper.user;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.user.Portfolio;
import com.web.editor.model.dto.user.PortfolioInfo;
import com.web.editor.model.dto.user.PortfolioNicknameUpdateRequest;
import com.web.editor.model.dto.user.PortfolioReview;
import com.web.editor.model.dto.user.PortfolioReviewSaveRequest;
import com.web.editor.model.dto.user.PortfolioSchedule;
import com.web.editor.model.dto.user.PortfolioScheduleDeleteRequest;
import com.web.editor.model.dto.user.PortfolioScheduleSaveRequest;
import com.web.editor.model.dto.user.PortfolioTag;
import com.web.editor.model.dto.user.PortfolioTagSaveRequest;
import com.web.editor.model.dto.user.PortfolioVideo;
import com.web.editor.model.dto.user.PortfolioVideoDeleteRequest;
import com.web.editor.model.dto.user.PortfolioVideoSaveRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class PortfolioMapper {
    @Autowired
    @Qualifier("firstSqlSessionTemplate")
    private SqlSession sqlSession;
   
    // 포트폴리오 조회
    public Portfolio findPortfolioByUid(String uid) throws SQLException{
        return sqlSession.selectOne("findPortfolioByUid", uid);
    }
    
    // 영상 조회
    public List<PortfolioVideo> findVideoByUid(String uid) throws SQLException{
        return sqlSession.selectList("findVideoByUid", uid);
    }
    
    // 태그 조회
    public List<PortfolioTag> findTagByUid(String uid) throws SQLException{
        return sqlSession.selectList("findTagByUid", uid);
    }
    
    // 스케쥴 조회
    public List<PortfolioSchedule> findScheduleByUid(String uid) throws SQLException{
        return sqlSession.selectList("findScheduleByUid", uid);
    }
    
    
    // 포트폴리오 초기 등록
    // uid를 이용해 uid, nickname 설정
    public int portfolioInitSave(String uid) throws SQLException{
        return sqlSession.insert("portfolioInitSave", uid);
    }

    // 영상 등록
    public int videoSave(PortfolioVideoSaveRequest portfolioVideoSaveRequest) throws SQLException{
		return sqlSession.insert("videoSave", portfolioVideoSaveRequest);
	}
    
    // 태그 등록
    public int tagSave(PortfolioTagSaveRequest portfolioTagSaveRequest) throws SQLException{
		return sqlSession.insert("tagSave", portfolioTagSaveRequest);
	}
    
    // 스케쥴 등록
    public int scheduleSave(PortfolioScheduleSaveRequest scheduleSaveRequest) throws SQLException{
		return sqlSession.insert("scheduleSave", scheduleSaveRequest);
	}
    
    // 포트폴리오 수정
    public int portfolioUpdate(PortfolioInfo portfolioInfo) throws SQLException {
        return sqlSession.update("portfolioUpdate", portfolioInfo);
    }

    // 스케줄 삭제
    public int deleteSchedule(PortfolioScheduleDeleteRequest portfolioScheduleDeleteRequest) throws SQLException{
        return sqlSession.delete("deleteSchedule", portfolioScheduleDeleteRequest);
    }
    
    // 영상 모두 삭제
    public int deleteVideos(PortfolioVideoDeleteRequest portfolioVideoDeleteRequest) throws SQLException{
		return sqlSession.delete("deleteVideos", portfolioVideoDeleteRequest);
    }
    
    // 태그 모두 삭제
    public int deleteTags(int uid) throws SQLException {
        return sqlSession.delete("deleteTags", uid);
    }

    
    public int portfolioNicknameUpdate(PortfolioNicknameUpdateRequest portfolioNicknameUpdateRequest) throws SQLException {
        return sqlSession.update("portfolioNicknameUpdate", portfolioNicknameUpdateRequest);
    }
}