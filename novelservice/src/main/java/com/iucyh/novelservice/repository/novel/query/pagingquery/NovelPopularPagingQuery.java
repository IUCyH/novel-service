package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.NovelPopularQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelPopularQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelPopularCursor;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;
import static com.iucyh.novelservice.domain.novel.QNovelPeriodView.novelPeriodView;

@Component
public class NovelPopularPagingQuery extends NovelPagingQueryBaseTemplate {

    @Override
    protected JPAQuery<? extends NovelPagingQueryDto> createBaseQuery(JPAQueryFactory queryFactory, NovelCursor cursor) {
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
    protected BooleanExpression createCursorPredicate(NovelCursor cursor) {
        NovelPopularCursor novelPopularCursor = (NovelPopularCursor) cursor;
        return novelPeriodView.viewCount.lt(novelPopularCursor.getLastAggViewCount())
                .or(
                        novelPeriodView.viewCount.eq(novelPopularCursor.getLastAggViewCount())
                                .and(novelPeriodView.id.lt(novelPopularCursor.getLastAggId()))
                );
    }

    @Override
    public NovelCursor createCursor(NovelPagingQueryDto lastResult) {
        NovelPopularQueryDto popularQueryDto = (NovelPopularQueryDto) lastResult;
        return new NovelPopularCursor(popularQueryDto.getLastAggId(), popularQueryDto.getLastAggViewCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.POPULAR;
    }
}
