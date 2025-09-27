package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.novel.query.NovelSimpleQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelLastUpdateCursor;
import com.iucyh.novelservice.service.novel.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

public class NovelLastUpdatePagingQuery extends NovelPagingQueryBaseTemplate<NovelLastUpdateCursor, NovelSimpleQueryDto> {

    @Override
    protected JPAQuery<NovelSimpleQueryDto> createBaseQuery(JPAQueryFactory queryFactory, NovelLastUpdateCursor cursor) {
        return queryFactory
                .select(new QNovelSimpleQueryDto(novel))
                .from(novel);
    }

    @Override
    protected OrderSpecifier<?>[] createOrderSpecifiers() {
        return new OrderSpecifier[] {
                novel.lastEpisodeAt.desc(),
                novel.id.desc()
        };
    }

    @Override
    protected BooleanExpression createCursorPredicate(NovelLastUpdateCursor cursor) {
        return novel.lastEpisodeAt.lt(cursor.getLastUpdateDate())
                .or(
                        novel.lastEpisodeAt.eq(cursor.getLastUpdateDate())
                                .and(novel.id.lt(cursor.getLastNovelId()))
                );
    }

    @Override
    public NovelLastUpdateCursor createCursor(NovelSimpleQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelLastUpdateCursor(lastNovel.getId(), lastNovel.getLastEpisodeAt());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LAST_UPDATE;
    }
}
