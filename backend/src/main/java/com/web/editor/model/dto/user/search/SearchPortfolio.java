package com.web.editor.model.dto.user.search;

import java.util.List;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchPortfolio {
    private int uid;
    private String nickname;
    private String url;
    private int payMin;
    private int bookmarkNumber;
    private int avgScore;
    private List<String> tags;
}