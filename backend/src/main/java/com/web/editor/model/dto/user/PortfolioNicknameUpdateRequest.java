package com.web.editor.model.dto.user;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Valid
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioNicknameUpdateRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private int uid;

    @ApiModelProperty(required = true)
    @NotNull
    private String nickname;

}