package com.web.editor.model.dto.user.schedule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HolidayDate {
    private String title = "holiday";
    private String start;
    private String end;
    private String repeat = "montly";
    private int categoryId;
}