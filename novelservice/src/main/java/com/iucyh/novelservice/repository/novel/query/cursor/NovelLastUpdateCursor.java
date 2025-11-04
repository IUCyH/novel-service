package com.iucyh.novelservice.repository.novel.query.cursor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NovelLastUpdateCursor implements NovelCursor {

    private final long lastNovelId;
    private final LocalDateTime lastUpdateDate;
}
