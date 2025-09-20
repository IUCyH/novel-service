package com.iucyh.novelservice.repository.novel.query.cursor;

import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface NovelCursor {

    JPAQuery<NovelPagingQueryDto> createPagingQuery(JPAQueryFactory queryFactory);
}
