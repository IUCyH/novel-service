package com.iucyh.novelservice.novel.repository.query.pagingquery;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.repository.dto.NovelPagingQueryDto;
import com.iucyh.novelservice.novel.repository.dto.NovelSimpleQueryDto;
import com.iucyh.novelservice.novel.repository.dto.QNovelSimpleQueryDto;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelLastUpdateCursor;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.novel.domain.QNovel.novel;

@Component
public class NovelLastUpdatePagingQuery extends NovelPagingQueryBaseTemplate {

    @Override
    protected JPAQuery<? extends NovelPagingQueryDto> createBaseQuery(JPAQueryFactory queryFactory) {
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
    protected BooleanExpression createCursorPredicate(NovelCursor cursor) {
        NovelLastUpdateCursor lastUpdateCursor = (NovelLastUpdateCursor) cursor;
        return novel.lastEpisodeAt.lt(lastUpdateCursor.lastUpdateDate())
                .or(
                        novel.lastEpisodeAt.eq(lastUpdateCursor.lastUpdateDate())
                                .and(novel.id.lt(lastUpdateCursor.lastNovelId()))
                );
    }

    @Override
    public NovelCursor createCursor(NovelPagingQueryDto lastResult) {
        Novel lastNovel = lastResult.getNovel();
        return new NovelLastUpdateCursor(lastNovel.getId(), lastNovel.getLastEpisodeAt());
    }

    @Override
    public NovelSortType getSupportedSortType() {
        return NovelSortType.LAST_UPDATE;
    }
}
