package com.web.editor.model.dto.user;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Valid
@ToString
@Getter
@Setter
public class PortfolioReviewSaveRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private String portfolioNickname;

    @ApiModelProperty(required = true)
    @NotNull
    private String nickname;

    @ApiModelProperty(required = true)
    @NotNull
    private int videoScore;

    @ApiModelProperty(required = true)
    @NotNull
    private int kindnessScore;

    @ApiModelProperty(required = true)
    @NotNull
    private int finishScore;

    @ApiModelProperty(required = true)
    @NotNull
    private String comment;
}