package com.web.editor.model.dto.request;

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
public class RequestReviewSaveRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private int requestFormRid;

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

    private float scoreAvg;
}