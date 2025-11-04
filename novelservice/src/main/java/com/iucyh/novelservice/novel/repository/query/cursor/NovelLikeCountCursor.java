package com.iucyh.novelservice.novel.repository.query.cursor;

public record NovelLikeCountCursor(

        long lastNovelId,
        int lastLikeCount
) implements NovelCursor {}
