package com.web.editor.model.dto.user;

import java.util.Date;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortfolioReview {
    private int reviewNo;
    private String nickname;
    private Date createdDate;
    private int videoScore;
    private int kindnessScore;
    private int finishScore;
    private String comment;
    private float scoreAvg;
}