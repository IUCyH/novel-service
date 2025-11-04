package com.iucyh.novelservice.novel.repository.query.pagingquery;

import com.iucyh.novelservice.novel.repository.query.dto.NovelPagingQueryDto;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface NovelPagingQuery {
    JPAQuery<? extends NovelPagingQueryDto> createQuery(JPAQueryFactory queryFactory, NovelCursor cursor);
    NovelCursor createCursor(NovelPagingQueryDto lastResult);
    NovelSortType getSupportedSortType();
}
