package com.web.editor.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameUpdateDto {
    private String orgNickname;
    private String nickname;
}