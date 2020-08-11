package com.web.editor.model.dto.user.search;

import java.util.List;

import com.web.editor.util.constants.SearchType;

import io.swagger.annotations.ApiModelProperty;

import com.web.editor.util.constants.SearchSortType;

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
    private String videoType;
    private List<String> videoSkills;
    private String videoStyle;

    // 검색어
    // tag, nickname
    private SearchType searchType;
    private String searchText;
    private List<String> searchTags;

    // 정렬 방식
    @ApiModelProperty(required = true)
    private SearchSortType sortType; 
}