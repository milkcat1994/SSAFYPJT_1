package com.web.editor.model.dto.user;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Repository;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Repository
public class PortfolioInfo {
    @ApiModelProperty(required = true)
    @NotNull
    private int uid;

    @ApiModelProperty(required = true)
    @NotNull
    private String skill;

    @ApiModelProperty(required = true)
    @NotNull
    private int payMin;

    @ApiModelProperty(required = true)
    @NotNull
    private String description;
}