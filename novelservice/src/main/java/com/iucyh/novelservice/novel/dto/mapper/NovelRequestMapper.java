package com.iucyh.novelservice.novel.dto.mapper;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.dto.request.CreateNovelRequest;

public class NovelRequestMapper {

    private NovelRequestMapper() {}

    public static Novel toNovel(CreateNovelRequest request) {
        NovelCategory category = NovelCategory.of(request.category());
        return Novel.of(request.title(), request.description(), category);
    }
}
