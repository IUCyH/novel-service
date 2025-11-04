package com.iucyh.novelservice.novel.repository.query.pagingquery.strategy;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.query.dto.NovelQueryDto;
import com.iucyh.novelservice.novel.repository.query.dto.QNovelSimpleQueryDto;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelLikeCountCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.pagingquery.NovelPagingQueryBaseTemplate;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Component
public class NovelLikeCountPagingQuery extends NovelPagingQueryBaseTemplate {

    @Override
    protected JPAQuery<? extends NovelQueryDto> createBaseQuery(JPAQueryFactory queryFactory) {
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
        return novel.likeCount.lt(likeCountCursor.lastLikeCount())
                .or(
                        novel.likeCount.eq(likeCountCursor.lastLikeCount())
                                .and(novel.id.lt(likeCountCursor.lastNovelId()))
                );
    }

    @Override
    public NovelCursor createCursor(NovelQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelLikeCountCursor(lastNovel.getId(), lastNovel.getLikeCount());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LIKE_COUNT;
    }
}
