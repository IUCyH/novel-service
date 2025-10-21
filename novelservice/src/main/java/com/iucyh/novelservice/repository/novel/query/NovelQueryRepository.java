package com.iucyh.novelservice.repository.novel.query;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelPagingQuery;

import java.util.List;

public interface NovelQueryRepository {

    List<? extends NovelPagingQueryDto> findNovels(NovelSearchCondition condition, NovelPagingQuery pagingQuery, int limit);
    List<? extends NovelPagingQueryDto> findNovelsByCategory(NovelSearchCondition condition, NovelPagingQuery pagingQuery, NovelCategory category, int limit);

    /**
     * 이번달 신작 소설 조회 메서드
     */
    List<? extends NovelPagingQueryDto> findNewNovels(NovelSearchCondition condition, NovelPagingQuery pagingQuery, int limit);
}
