package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.novel.query.NovelSimpleQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelLikeCountCursor;
import com.iucyh.novelservice.service.novel.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

public class NovelLikeCountPagingQuery extends NovelPagingQueryBaseTemplate<NovelLikeCountCursor, NovelSimpleQueryDto> {

    @Override
    protected JPAQuery<NovelSimpleQueryDto> createBaseQuery(JPAQueryFactory queryFactory, NovelLikeCountCursor cursor) {
        return queryFactory
                .select(new QNovelSimpleQueryDto(novel))
                .from(novel);
    }

    @Override
    protected OrderSpecifier<?>[] createOrderSpecifiers() {
        return new OrderSpecifier[] {
                novel.likeCount.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression createCursorPredicate(NovelLikeCountCursor cursor) {
        return novel.likeCount.lt(cursor.getLastLikeCount())
                .or(
                        novel.likeCount.eq(cursor.getLastLikeCount())
                                .and(novel.id.lt(cursor.getLastNovelId()))
                );
    }

    @Override
    public NovelLikeCountCursor createCursor(NovelSimpleQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelLikeCountCursor(lastNovel.getId(), lastNovel.getLikeCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LIKE_COUNT;
    }
}
