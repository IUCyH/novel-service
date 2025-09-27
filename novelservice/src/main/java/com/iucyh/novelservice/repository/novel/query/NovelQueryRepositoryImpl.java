package com.iucyh.novelservice.repository.novel.query;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelPopularCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelPagingQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.iucyh.novelservice.domain.novel.QNovel.novel;

@Repository
@RequiredArgsConstructor
public class NovelQueryRepositoryImpl implements NovelQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public <T extends NovelCursor, R extends NovelPagingQueryDto> List<R> findNovels(
            NovelSearchCondition<T> condition,
            NovelPagingQuery<T, R> pagingQuery,
            int limit
    ) {
        return pagingQuery
                .createQuery(queryFactory, condition.getCursor())
                .where(novel.deletedAt.isNull())
                .limit(limit)
                .fetch();
    }

    @Override
    public <T extends NovelCursor, R extends NovelPagingQueryDto> List<R> findNovelsByCategory(
            NovelSearchCondition<T> condition,
            NovelPagingQuery<T, R> pagingQuery,
            NovelCategory category,
            int limit
    ) {
        return pagingQuery
                .createQuery(queryFactory, condition.getCursor())
                .where(
                        novel.deletedAt.isNull(),
                        novel.category.eq(category)
                )
                .limit(limit)
                .fetch();
    }

    @Override
    public <T extends NovelCursor, R extends NovelPagingQueryDto> List<R> findNewNovels(
            NovelSearchCondition<T> condition,
            NovelPagingQuery<T, R> pagingQuery,
            int limit
    ) {
        LocalDateTime thisMonth = getThisMonth();
        return pagingQuery
                .createQuery(queryFactory, condition.getCursor())
                .where(
                        novel.deletedAt.isNull(),
                        novel.lastEpisodeAt.goe(thisMonth)
                )
                .limit(limit)
                .fetch();
    }

    private LocalDateTime getThisMonth() {
        return LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
