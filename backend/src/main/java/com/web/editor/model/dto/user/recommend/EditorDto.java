package com.web.editor.model.dto.user.recommend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorDto {
    private String uid;
    private String tag;
    private String skill;
    private double similarity;

    private String url;
    private String nickname;
    private String pay;
    private String description;

}