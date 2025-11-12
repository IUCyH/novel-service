package com.iucyh.novelservice.novel.web.dto.mapper;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.web.dto.request.CreateNovelRequest;
import com.iucyh.novelservice.user.domain.User;

public class NovelRequestMapper {

    private NovelRequestMapper() {}

    public static Novel toNovel(CreateNovelRequest request) {
        NovelCategory category = NovelCategory.of(request.category());
        // TODO: 실제 User 엔티티 받도록 수정
        User user = User.of("abc@example.com", "abc123", "lucy", "test");
        return Novel.of(request.title(), request.description(), category, user);
    }
}
