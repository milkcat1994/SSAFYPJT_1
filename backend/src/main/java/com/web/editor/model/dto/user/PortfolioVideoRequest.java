package com.web.editor.model.dto.user;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Repository;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Repository
public class PortfolioVideoRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private int portfolioUid;

    @ApiModelProperty(required = true)
    @NotNull
    private List<String> url;

    @ApiModelProperty(required = true)
    @NotNull
    private int mainFlag;
}