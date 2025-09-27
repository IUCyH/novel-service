package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.novel.query.NovelSimpleQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelViewCountCursor;
import com.iucyh.novelservice.service.novel.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

public class NovelViewCountPagingQuery extends NovelPagingQueryBaseTemplate<NovelViewCountCursor, NovelSimpleQueryDto> {

    @Override
    protected JPAQuery<NovelSimpleQueryDto> createBaseQuery(JPAQueryFactory queryFactory, NovelViewCountCursor cursor) {
        return queryFactory
                .select(new QNovelSimpleQueryDto(novel))
                .from(novel);
    }

    @Override
    protected OrderSpecifier<?>[] createOrderSpecifiers() {
        return new OrderSpecifier[] {
                novel.totalViewCount.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression createCursorPredicate(NovelViewCountCursor cursor) {
        return novel.totalViewCount.lt(cursor.getLastTotalViewCount())
                .or(
                        novel.totalViewCount.eq(cursor.getLastTotalViewCount())
                                .and(novel.id.lt(cursor.getLastNovelId()))
                );
    }

    @Override
    public NovelViewCountCursor createCursor(NovelSimpleQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelViewCountCursor(lastNovel.getId(), lastNovel.getTotalViewCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.VIEW_COUNT;
    }
}
