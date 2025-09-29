package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface NovelPagingQuery {
    JPAQuery<? extends NovelPagingQueryDto> createQuery(JPAQueryFactory queryFactory, NovelCursor cursor);
    NovelCursor createCursor(NovelPagingQueryDto lastResult);
    NovelSortType getSupportedSortType();
}
