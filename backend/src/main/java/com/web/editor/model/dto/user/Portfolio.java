package com.web.editor.model.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Portfolio {
    private int uid;
    private String nickname;
    private String skill;
    private int payMin;
    private String description;
}