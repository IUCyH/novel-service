package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelLikeCountCursor;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

@Component
public class NovelLikeCountPagingQuery extends NovelPagingQueryBaseTemplate {

    @Override
    protected JPAQuery<? extends NovelPagingQueryDto> createBaseQuery(JPAQueryFactory queryFactory) {
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
    protected BooleanExpression createCursorPredicate(NovelCursor cursor) {
        NovelLikeCountCursor likeCountCursor = (NovelLikeCountCursor) cursor;
        return novel.likeCount.lt(likeCountCursor.getLastLikeCount())
                .or(
                        novel.likeCount.eq(likeCountCursor.getLastLikeCount())
                                .and(novel.id.lt(likeCountCursor.getLastNovelId()))
                );
    }

    @Override
    public NovelCursor createCursor(NovelPagingQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelLikeCountCursor(lastNovel.getId(), lastNovel.getLikeCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LIKE_COUNT;
    }
}
