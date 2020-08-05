package com.web.editor.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyDto {
    private int nid;
    private String request_nickname;
    private String response_nickname;
    private String notify_type;
    private String created_date;
    private int read_flag; 
}