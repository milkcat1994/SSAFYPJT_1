package com.web.editor.model.dto.user.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class SearchPortfolio{
    private int uid;
    private String nickname;
    private String url;
    private int payMin;
    private int bookmarkNumber;
    private boolean togleBookmark = false;
    private float avgScore;
    private List<String> tags;

    public SearchPortfolio(SearchPortfolioJoinBookmark searchPortfolioJoinBookmark, Set<String> tags){
        this.uid = searchPortfolioJoinBookmark.getUid();
        this.nickname = searchPortfolioJoinBookmark.getNickname();
        this.url = searchPortfolioJoinBookmark.getUrl();
        this.payMin = searchPortfolioJoinBookmark.getPayMin();
        this.bookmarkNumber = searchPortfolioJoinBookmark.getBookmarkNumber();
        this.avgScore = searchPortfolioJoinBookmark.getAvgScore();
        this.tags = new ArrayList<String>();
        this.tags.addAll(tags);
    }
    
    public SearchPortfolio(SearchPortfolioJoinBookmark searchPortfolioJoinBookmark){
        this.uid = searchPortfolioJoinBookmark.getUid();
        this.nickname = searchPortfolioJoinBookmark.getNickname();
        this.url = searchPortfolioJoinBookmark.getUrl();
        this.payMin = searchPortfolioJoinBookmark.getPayMin();
        this.bookmarkNumber = searchPortfolioJoinBookmark.getBookmarkNumber();
        this.avgScore = searchPortfolioJoinBookmark.getAvgScore();
    }
}