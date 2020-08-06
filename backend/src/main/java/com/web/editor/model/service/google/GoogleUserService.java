package com.web.editor.model.service.google;

import com.web.editor.model.dto.user.User;

public interface GoogleUserService {
    // 구글 로그인
    public User googleLogin(String email);

    public int googleConnect(int uid);
}