package com.web.editor.model.dto.user;

import java.util.Date;

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
public class User {
    private int uid;
    private String platformType;
    private String token;
    private String email;
    private String password;
    private String nickname;
    private String auth;
    private Date createdDate;

    public User(String email, String nickname){
        this.email = email;
        this.nickname = nickname;
    }

    public User(int uid, String nickname, String auth){
        this.uid = uid;
        this.nickname = nickname;
        this.nickname = auth;
    }

    public User(String email, String password, String nickname, String platformType, String auth){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.platformType = platformType;
        this.auth = auth;
    }
}