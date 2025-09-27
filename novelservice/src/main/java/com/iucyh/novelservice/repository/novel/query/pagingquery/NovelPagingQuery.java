package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.service.novel.NovelSortType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface NovelPagingQuery<T extends NovelCursor, R extends NovelPagingQueryDto> {
    JPAQuery<R> createQuery(JPAQueryFactory queryFactory, T cursor);
    T createCursor(R lastResult);
    NovelSortType getSupportedSortType();
}
