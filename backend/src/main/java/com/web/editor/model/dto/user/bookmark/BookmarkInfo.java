package com.web.editor.model.dto.user.bookmark;

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
public class BookmarkInfo {
    private int userInfoUid;
    private int markUid;
}