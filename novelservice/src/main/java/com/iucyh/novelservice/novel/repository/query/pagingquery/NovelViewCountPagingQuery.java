package com.iucyh.novelservice.novel.repository.query.pagingquery;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.dto.NovelPagingQueryDto;
import com.iucyh.novelservice.novel.repository.dto.QNovelSimpleQueryDto;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelViewCountCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Component
public class NovelViewCountPagingQuery extends NovelPagingQueryBaseTemplate {

    @Override
    protected JPAQuery<? extends NovelPagingQueryDto> createBaseQuery(JPAQueryFactory queryFactory) {
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
        return novel.totalViewCount.lt(novelViewCountCursor.lastTotalViewCount())
                .or(
                        novel.totalViewCount.eq(novelViewCountCursor.lastTotalViewCount())
                                .and(novel.id.lt(novelViewCountCursor.lastNovelId()))
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
