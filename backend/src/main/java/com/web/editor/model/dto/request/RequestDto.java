package com.web.editor.model.dto.request;

import lombok.Setter;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
@Setter
public class RequestDto {
	private int rid;
	@NotNull
	private String request_nickname;
	@NotNull
	private String response_nickname;
	@NotNull
	private String video_type;
	@NotNull
	private String video_origin_length;
	@NotNull
	private String video_result_length;
	@NotNull
	private String video_style;
	@NotNull
	private String video_skill;	// DB저장 및 front 알림 페이지 출력용
	private String request_date;
	private String response_date;
	@NotNull
	private String start_date;
	@NotNull
	private String end_date;
	private String request_description;
	private int done_flag;

	private String tag_list; // front에서 출력용, #태그1 #태그2 형식으로 출력
	
	private List<String> tags;	// 등록용, front에서 배열로 넘어옴

	private int idx;
}
