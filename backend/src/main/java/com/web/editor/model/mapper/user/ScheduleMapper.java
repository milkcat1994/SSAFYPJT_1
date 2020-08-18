package com.web.editor.model.mapper.user;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.user.schedule.HolidayDate;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleMapper {
    @Qualifier("firstSqlSessionTemplate")
    
    @Autowired
    SqlSession sqlSession;
    
    // 편집자 회원의 휴일 일정 조회
	public List<HolidayDate> holidayDates(int uid) throws SQLException{
		return sqlSession.selectList("holidayDates", uid);
	};

}