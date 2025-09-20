package com.iucyh.novelservice.repository.novel.query.cursor;

import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelPagingQueryDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

@Getter
@RequiredArgsConstructor
public final class NovelRecentUpdateCursor implements NovelCursor {

    private final LocalDateTime lastUpdateDate;
    private final long lastId;

    @Override
    public JPAQuery<NovelPagingQueryDto> createPagingQuery(JPAQueryFactory queryFactory) {
        return queryFactory
                .select(
                        new QNovelPagingQueryDto(
                                novel,
                                null
                        )
                )
                .from(novel)
                .where(
                        novel.lastEpisodeAt.lt(this.lastUpdateDate)
                                .or(
                                        novel.lastEpisodeAt.eq(this.lastUpdateDate)
                                                .and(novel.id.gt(this.lastId))
                                )
                )
                .orderBy(
                        novel.lastEpisodeAt.desc(),
                        novel.id.asc()
                );
    }
}
