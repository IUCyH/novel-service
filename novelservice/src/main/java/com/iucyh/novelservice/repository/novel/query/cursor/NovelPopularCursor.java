package com.iucyh.novelservice.repository.novel.query.cursor;

import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.QNovelPagingQueryDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;
import static com.iucyh.novelservice.domain.novel.QNovelPeriodView.novelPeriodView;

@Getter
@RequiredArgsConstructor
public final class NovelPopularCursor implements NovelCursor {

    private final int lastWatchCount;
    private final long lastId;

    @Override
    public JPAQuery<NovelPagingQueryDto> createPagingQuery(JPAQueryFactory queryFactory) {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);

        return queryFactory
                .select(
                        new QNovelPagingQueryDto(
                                novel,
                                novelPeriodView.id
                        )
                )
                .from(novelPeriodView)
                .leftJoin(novelPeriodView.novel, novel)
                .where(
                        novelPeriodView.startDate.eq(threeDaysAgo),
                        novelPeriodView.viewCount.lt(this.lastWatchCount)
                                .or(
                                        novelPeriodView.viewCount.eq(this.lastWatchCount)
                                                .and(novelPeriodView.id.gt(this.lastId))
                                )
                )
                .orderBy(
                        novelPeriodView.viewCount.desc(),
                        novelPeriodView.id.asc()
                );
    }
}
