package com.web.editor.model.dto.request;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class RequestDto {
	private int rid;
	private String request_nickname;
	private String response_nickname;
	private String video_type;
	private String video_origin_length;
	private String video_result_length;
	private String video_style;
	private String video_skill;
	private String request_date;
	private String response_date;
	private String start_date;
	private String end_date;
	private String request_description;
	private int done_flag;

	private String tag_list;

	
}
