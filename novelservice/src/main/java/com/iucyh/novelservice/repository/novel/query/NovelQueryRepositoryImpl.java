package com.iucyh.novelservice.repository.novel.query;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelPopularCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelPagingQuery;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public List<? extends NovelPagingQueryDto> findNovels(NovelSearchCondition condition, NovelPagingQuery pagingQuery, int limit) {
        return pagingQuery
                .createQuery(queryFactory, condition.getCursor())
                .where(getDefaultFilterCondition())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<? extends NovelPagingQueryDto> findNovelsByCategory(NovelSearchCondition condition, NovelPagingQuery pagingQuery, NovelCategory category, int limit) {
        return pagingQuery
                .createQuery(queryFactory, condition.getCursor())
                .where(
                        getDefaultFilterCondition(),
                        novel.category.eq(category)
                )
                .limit(limit)
                .fetch();
    }

    @Override
    public List<? extends NovelPagingQueryDto> findNewNovels(NovelSearchCondition condition, NovelPagingQuery pagingQuery, int limit) {
        LocalDateTime thisMonth = getThisMonth();
        return pagingQuery
                .createQuery(queryFactory, condition.getCursor())
                .where(
                        getDefaultFilterCondition(),
                        novel.createdAt.goe(thisMonth)
                )
                .limit(limit)
                .fetch();
    }

    private BooleanExpression getDefaultFilterCondition() {
        return novel.deletedAt.isNull() // 삭제되지 않은 소설
                .and(novel.lastEpisodeAt.isNotNull()); // 회차가 하나라도 존재하는 소설
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
