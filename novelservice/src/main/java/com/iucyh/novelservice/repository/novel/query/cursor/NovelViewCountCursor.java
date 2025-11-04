package com.iucyh.novelservice.repository.novel.query.cursor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NovelViewCountCursor implements NovelCursor {

    private final long lastNovelId;
    private final int lastTotalViewCount;
}
