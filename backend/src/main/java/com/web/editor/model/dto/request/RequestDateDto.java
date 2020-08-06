package com.web.editor.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDateDto {
    private String title = "In Progress";
    private String start;
    private String end;
    private String repeat = "montly";
    private int categoryId = 3;

}