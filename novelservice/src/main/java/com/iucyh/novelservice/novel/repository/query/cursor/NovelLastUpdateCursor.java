package com.iucyh.novelservice.novel.repository.query.cursor;

import java.time.LocalDateTime;

public record NovelLastUpdateCursor(
        long lastNovelId,
        LocalDateTime lastUpdateDate
) implements NovelCursor {}