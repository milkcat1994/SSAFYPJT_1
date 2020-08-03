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
public class UserUpdateRequest {
    @ApiModelProperty(required = true)
    @NotNull
    String nickname;
}
