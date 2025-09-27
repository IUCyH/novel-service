package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.dto.novel.query.NovelPopularQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelPopularQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelPopularCursor;
import com.iucyh.novelservice.service.novel.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDate;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;
import static com.iucyh.novelservice.domain.novel.QNovelPeriodView.novelPeriodView;

public class NovelPopularPagingQuery extends NovelPagingQueryBaseTemplate<NovelPopularCursor, NovelPopularQueryDto> {

    @Override
    protected JPAQuery<NovelPopularQueryDto> createBaseQuery(JPAQueryFactory queryFactory, NovelPopularCursor cursor) {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
        return queryFactory
                .select(
                        new QNovelPopularQueryDto(
                                novel,
                                novelPeriodView.id,
                                novelPeriodView.viewCount
                        )
                )
                .from(novelPeriodView)
                .join(novelPeriodView.novel, novel)
                .where(
                        novelPeriodView.startDate.eq(threeDaysAgo)
                );
    }

    @Override
    protected OrderSpecifier<?>[] createOrderSpecifiers() {
        return new OrderSpecifier[] {
                novelPeriodView.viewCount.desc(),
                novelPeriodView.id.desc()
        };
    }

    @Override
    protected BooleanExpression createCursorPredicate(NovelPopularCursor cursor) {
        return novelPeriodView.viewCount.lt(cursor.getLastAggViewCount())
                .or(
                        novelPeriodView.viewCount.eq(cursor.getLastAggViewCount())
                                .and(novelPeriodView.id.lt(cursor.getLastAggId()))
                );
    }

    @Override
    public NovelPopularCursor createCursor(NovelPopularQueryDto lastResult) {
        return new NovelPopularCursor(lastResult.getLastAggId(), lastResult.getLastAggViewCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.POPULAR;
    }
}
