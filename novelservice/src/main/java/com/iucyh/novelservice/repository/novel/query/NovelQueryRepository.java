package com.iucyh.novelservice.repository.novel.query;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelPagingQuery;

import java.util.List;

public interface NovelQueryRepository {

    <T extends NovelCursor, R extends NovelPagingQueryDto> List<R> findNovels(
            NovelSearchCondition<T> condition,
            NovelPagingQuery<T, R> pagingQuery,
            int limit
    );
    <T extends NovelCursor, R extends NovelPagingQueryDto> List<R> findNovelsByCategory(
            NovelSearchCondition<T> condition,
            NovelPagingQuery<T, R> pagingQuery,
            NovelCategory category,
            int limit
    );

    /**
     * 이번달 신작 소설 조회 메서드
     */
    <T extends NovelCursor, R extends NovelPagingQueryDto> List<R> findNewNovels(
            NovelSearchCondition<T> condition,
            NovelPagingQuery<T, R> pagingQuery,
            int limit
    );
}
