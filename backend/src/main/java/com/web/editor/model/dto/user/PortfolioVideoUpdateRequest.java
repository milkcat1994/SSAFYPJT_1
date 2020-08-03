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
public class PortfolioVideoUpdateRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private List<PortfolioVideoSaveRequest> portfolioVideo;
}