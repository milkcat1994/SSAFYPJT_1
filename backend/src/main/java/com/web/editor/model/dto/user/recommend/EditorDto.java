package com.web.editor.model.dto.user.recommend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorDto {
    private String uid;
    private String tags;
    private String skill;
    private String pay;
    private double similarity;
}