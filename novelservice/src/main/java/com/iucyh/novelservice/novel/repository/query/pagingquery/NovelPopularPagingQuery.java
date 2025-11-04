package com.iucyh.novelservice.novel.repository.query.pagingquery;

import com.iucyh.novelservice.novel.repository.query.dto.NovelPagingQueryDto;
import com.iucyh.novelservice.novel.repository.query.dto.NovelPopularQueryDto;
import com.iucyh.novelservice.novel.repository.dto.QNovelPopularQueryDto;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelPopularCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;
import static com.iucyh.novelservice.novel.domain.QNovelPeriodView.novelPeriodView;

@Component
public class NovelPopularPagingQuery extends NovelPagingQueryBaseTemplate {

    @Override
    protected JPAQuery<? extends NovelPagingQueryDto> createBaseQuery(JPAQueryFactory queryFactory) {
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
        return novelPeriodView.viewCount.lt(novelPopularCursor.lastAggViewCount())
                .or(
                        novelPeriodView.viewCount.eq(novelPopularCursor.lastAggViewCount())
                                .and(novelPeriodView.id.lt(novelPopularCursor.lastAggId()))
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
