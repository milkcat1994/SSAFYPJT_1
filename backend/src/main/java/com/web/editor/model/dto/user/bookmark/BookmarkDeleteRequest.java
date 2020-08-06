package com.web.editor.model.dto.user.bookmark;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BookmarkDeleteRequest {
    @ApiModelProperty(required = true)
    // @NotNull
    int uid;

    @ApiModelProperty(required = true)
    // @NotNull
    int muid;
}