package com.iucyh.novelservice.repository.novel.query.cursor;

import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelPagingQueryDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

@Getter
@RequiredArgsConstructor
public class NovelLikeCountCursor implements NovelCursor {

    private final int lastLikeCount;
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
                        novel.likeCount.lt(this.lastLikeCount)
                                .or(
                                        novel.likeCount.eq(this.lastLikeCount)
                                                .and(novel.id.gt(this.lastId))
                                )
                )
                .orderBy(
                        novel.likeCount.desc(),
                        novel.id.asc()
                );
    }
}
