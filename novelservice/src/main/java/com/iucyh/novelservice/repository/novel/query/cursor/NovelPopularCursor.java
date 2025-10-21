package com.iucyh.novelservice.repository.novel.query.cursor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NovelPopularCursor implements NovelCursor {

    private final long lastAggId;
    private final int lastAggViewCount;
}
