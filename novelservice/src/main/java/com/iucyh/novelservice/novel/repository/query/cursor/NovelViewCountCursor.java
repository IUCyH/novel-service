package com.iucyh.novelservice.novel.repository.query.cursor;

public record NovelViewCountCursor(
        long lastNovelId,
        int lastTotalViewCount
) implements NovelCursor {}
