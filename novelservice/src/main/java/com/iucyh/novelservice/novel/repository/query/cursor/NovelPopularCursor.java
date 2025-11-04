package com.iucyh.novelservice.novel.repository.query.cursor;

public record NovelPopularCursor(

        long lastAggId,
        int lastAggViewCount
) implements NovelCursor {}
