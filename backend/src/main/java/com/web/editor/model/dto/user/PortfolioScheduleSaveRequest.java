package com.web.editor.model.dto.user;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
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
    private Date endDate;

    @ApiModelProperty(required = true)
    @NotNull
    private int scheduleType;
}