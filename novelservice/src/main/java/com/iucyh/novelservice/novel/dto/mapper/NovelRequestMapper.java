package com.iucyh.novelservice.novel.dto.mapper;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.dto.request.CreateNovelRequest;

public class NovelRequestMapper {

    private NovelRequestMapper() {}

    public static Novel toNovel(CreateNovelRequest novelDto) {
        NovelCategory category = NovelCategory.of(novelDto.category());
        return Novel.of(novelDto.title(), novelDto.description(), category);
    }
}
