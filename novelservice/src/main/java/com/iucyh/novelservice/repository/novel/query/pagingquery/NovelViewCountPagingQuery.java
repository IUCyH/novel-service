package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelViewCountCursor;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

@Component
public class NovelViewCountPagingQuery extends NovelPagingQueryBaseTemplate {

    @Override
    protected JPAQuery<? extends NovelPagingQueryDto> createBaseQuery(JPAQueryFactory queryFactory, NovelCursor cursor) {
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
    protected BooleanExpression createCursorPredicate(NovelCursor cursor) {
        NovelViewCountCursor novelViewCountCursor = (NovelViewCountCursor) cursor;
        return novel.totalViewCount.lt(novelViewCountCursor.getLastTotalViewCount())
                .or(
                        novel.totalViewCount.eq(novelViewCountCursor.getLastTotalViewCount())
                                .and(novel.id.lt(novelViewCountCursor.getLastNovelId()))
                );
    }

    @Override
    public NovelCursor createCursor(NovelPagingQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelViewCountCursor(lastNovel.getId(), lastNovel.getTotalViewCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.VIEW_COUNT;
    }
}
