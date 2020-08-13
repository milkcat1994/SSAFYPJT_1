package com.web.editor.model.service.user.schedule;

import java.sql.SQLException;
import java.util.List;

import com.web.editor.model.dto.user.schedule.HolidayDate;
import com.web.editor.model.mapper.user.ScheduleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayDateServiceImpl implements HolidayDateService {
    
    @Autowired
    ScheduleMapper scheduleMapper;

    // 일반회원의 휴일 일정 반환

    @Override
    public List<HolidayDate> holidayDates(int uid) {
        try {
            return scheduleMapper.holidayDates(uid);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
