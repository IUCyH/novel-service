package com.iucyh.novelservice.repository.novel.query;

import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NovelSearchCondition {

    private final NovelCursor cursor;
}
