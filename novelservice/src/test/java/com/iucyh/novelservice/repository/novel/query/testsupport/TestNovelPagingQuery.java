package com.iucyh.novelservice.repository.novel.query.testsupport;

import com.iucyh.novelservice.domain.novel.QNovel;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelPagingQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class TestNovelPagingQuery implements NovelPagingQuery {

    @Override
    public JPAQuery<? extends NovelPagingQueryDto> createQuery(JPAQueryFactory queryFactory, NovelCursor cursor) {
        return queryFactory
                .select(new QNovelSimpleQueryDto(QNovel.novel))
                .from(QNovel.novel);
    }

    @Override
    public NovelCursor createCursor(NovelPagingQueryDto lastResult) {
        return null;
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return null;
    }
}
