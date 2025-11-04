package com.iucyh.novelservice.novel.repository.query.pagingquery;

import com.iucyh.novelservice.novel.repository.query.dto.NovelQueryDto;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface NovelPagingQuery {

    JPAQuery<? extends NovelQueryDto> createQuery(JPAQueryFactory queryFactory, NovelCursor cursor);
    NovelCursor createCursor(NovelQueryDto lastResult);
    NovelSortType getSupportedSortType();
}
