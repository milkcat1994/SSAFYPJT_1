package com.web.editor.model.service.user.schedule;

import java.util.List;

import com.web.editor.model.dto.user.schedule.HolidayDate;

public interface HolidayDateService {

    // 편집자 회원의 개인일정 조회
    public List<HolidayDate> holidayDates(int uid);
    
    
}
