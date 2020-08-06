package com.web.editor.model.dto.user.bookmark;

import java.util.List;

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
// 북마크 리스트 불러오기
public class Bookmark {
    // 팔로잉
    // uid
    private int muid;
    // 닉네임
    private String nickname;
    // pay/min
    private String payMin;
    // 대표 영상 url
    private String mainUrl;

    private List<String> tags;
}