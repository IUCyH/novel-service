package com.iucyh.novelservice.repository.novel.query;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
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
    public List<NovelPagingQueryDto> findNovels(NovelSearchCondition condition, int limit) {
        JPAQuery<NovelPagingQueryDto> query = createPagingQuery(condition.getCursor());
        return query
                .where(novel.deletedAt.isNull())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<NovelPagingQueryDto> findNovelsByCategory(NovelSearchCondition condition, NovelCategory category, int limit) {
        JPAQuery<NovelPagingQueryDto> query = createPagingQuery(condition.getCursor());
        return query
                .where(
                        novel.deletedAt.isNull(),
                        novel.category.eq(category)
                )
                .limit(limit)
                .fetch();
    }

    @Override
    public List<NovelPagingQueryDto> findNewNovels(NovelSearchCondition condition, int limit) {
        LocalDateTime thisMonth = getThisMonth();
        JPAQuery<NovelPagingQueryDto> query = createPagingQuery(condition.getCursor());
        return query
                .where(
                        novel.deletedAt.isNull(),
                        novel.lastEpisodeAt.goe(thisMonth)
                )
                .limit(limit)
                .fetch();
    }

    private JPAQuery<NovelPagingQueryDto> createPagingQuery(NovelCursor cursor) {
        return cursor.createPagingQuery(queryFactory);
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
