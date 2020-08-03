package com.web.editor.model.dto.user;

import java.util.Date;

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
public class PortfolioScheduleSaveRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private int portfolioUid;

    @ApiModelProperty(required = true)
    @NotNull
    private Date startDate;

    @ApiModelProperty(required = true)
    @NotNull
    private int term;

    @ApiModelProperty(required = true)
    @NotNull
    private String scheduleType;
}