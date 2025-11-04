package com.iucyh.novelservice.novel.repository.query.pagingquery;

import com.iucyh.novelservice.novel.repository.query.dto.NovelPagingQueryDto;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * <p>가장 일반적인 패턴의 <b>Novel 페이징 쿼리 생성을 돕는 템플릿 클래스</b></p>
 * 기본쿼리.where(커서 적용).orderBy 패턴이라면 해당 클래스 상속 권장
 */
public abstract class NovelPagingQueryBaseTemplate implements NovelPagingQuery {

    /**
     * 정렬 조건, 커서 적용 을 제외한 <b>select/from 등 기본 쿼리 생성 메서드</b>
     */
    protected abstract JPAQuery<? extends NovelPagingQueryDto> createBaseQuery(JPAQueryFactory queryFactory);

    /**
     * 각 페이징 전략에 맞는 정렬 기준 생성 메서드
     */
    protected abstract OrderSpecifier<?>[] createOrderSpecifiers();

    /**
     * 커서가 적용된 predicate 생성 메서드
     */
    protected abstract BooleanExpression createCursorPredicate(NovelCursor cursor);

    @Override
    public JPAQuery<? extends NovelPagingQueryDto> createQuery(JPAQueryFactory queryFactory, NovelCursor cursor) {
        JPAQuery<? extends NovelPagingQueryDto> query = createBaseQuery(queryFactory)
                .orderBy(
                        createOrderSpecifiers()
                );
        if (isNotFirstPage(cursor)) {
            query.where(
                    createCursorPredicate(cursor)
            );
        }
        return query;
    }

    private boolean isNotFirstPage(NovelCursor cursor) {
        return cursor != null;
    }
}
