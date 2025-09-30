package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelLastUpdateCursor;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

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
        return novel.lastEpisodeAt.lt(lastUpdateCursor.getLastUpdateDate())
                .or(
                        novel.lastEpisodeAt.eq(lastUpdateCursor.getLastUpdateDate())
                                .and(novel.id.lt(lastUpdateCursor.getLastNovelId()))
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
