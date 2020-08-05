package com.web.editor.model.dto.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioScheduleDeleteRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private int uid;

    @ApiModelProperty(required = true)
    @NotNull
    private int scheduleType;
}