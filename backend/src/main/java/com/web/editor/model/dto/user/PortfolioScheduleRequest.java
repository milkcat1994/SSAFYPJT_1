package com.web.editor.model.dto.user;

import java.util.Date;
import java.util.List;

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
public class PortfolioScheduleRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private int portfolioUid;

    @ApiModelProperty(required = true)
    @NotNull
    private List<Date> startDate;

    @ApiModelProperty(required = true)
    @NotNull
    private List<Date> endDate;

    @ApiModelProperty(required = true)
    @NotNull
    private int scheduleType;
}