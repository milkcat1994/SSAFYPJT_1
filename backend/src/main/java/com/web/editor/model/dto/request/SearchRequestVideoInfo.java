package com.web.editor.model.dto.request;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Repository
public class SearchRequestVideoInfo {
    String nickname;
    String videoType;
    String videoStyle;
    String videoSkill;
}