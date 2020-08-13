package com.web.editor.model.dto.user;

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
public class PortfolioList {
    private int uid;
    private String nickname;
    private int payMin;
    private List<String> URLs;
    private int bookmarkCount;
    private List<String> tags;
}
