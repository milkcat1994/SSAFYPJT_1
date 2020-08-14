package com.web.editor.model.dto.user.search;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// Front의 검색 요청 Object
public class SearchRequest {
    // 필터링
    // videoType, videoSkill, videoStyle
    private List<String> videoTypes;
    private List<String> videoSkills;
    private List<String> videoStyles;

    // 검색어
    // tag, nickname
    private String searchType;
    private String searchText;
    private List<String> searchTags;

    // 정렬 방식
    @ApiModelProperty(required = true)
    private String sortType; 
}