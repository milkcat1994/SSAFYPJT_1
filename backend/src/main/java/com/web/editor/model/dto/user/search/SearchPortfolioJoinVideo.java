package com.web.editor.model.dto.user.search;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Repository
public class SearchPortfolioJoinVideo {
    private int uid;
    private String mainUrl;

    public SearchPortfolioJoinVideo(SearchPortfolioJoinVideo searchPortfolioJoinVideo){
        this.uid = searchPortfolioJoinVideo.getUid();
        this.mainUrl = searchPortfolioJoinVideo.getMainUrl();
    }
}