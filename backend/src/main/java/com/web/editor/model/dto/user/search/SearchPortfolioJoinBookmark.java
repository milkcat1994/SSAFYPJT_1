package com.web.editor.model.dto.user.search;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Repository
@ToString
public class SearchPortfolioJoinBookmark {
    private Integer uid;
    private String nickname;
    private Integer payMin;
    private String url;
    // private String skill;
    private Integer bookmarkNumber;
    private float avgScore;
    private String tagKey;

    public void setInit(String url, String tagKey){
        this.url = url;
        this.tagKey = tagKey;
    }
}