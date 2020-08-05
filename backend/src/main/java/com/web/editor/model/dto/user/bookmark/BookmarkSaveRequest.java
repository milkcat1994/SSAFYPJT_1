package com.web.editor.model.dto.user.bookmark;

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
public class BookmarkSaveRequest {
    @ApiModelProperty(required = true)
    @NotNull
    int uid;

    @ApiModelProperty(required = true)
    @NotNull
    int muid;
}