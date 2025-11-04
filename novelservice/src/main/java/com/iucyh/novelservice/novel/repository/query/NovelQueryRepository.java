package com.iucyh.novelservice.novel.repository.query;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.repository.query.dto.NovelQueryDto;
import com.iucyh.novelservice.novel.repository.query.condition.NovelSearchCondition;
import com.iucyh.novelservice.novel.repository.query.pagingquery.NovelPagingQuery;

import java.util.List;

public interface NovelQueryRepository {

    List<? extends NovelQueryDto> findNovels(NovelSearchCondition condition, NovelPagingQuery pagingQuery);
    List<? extends NovelQueryDto> findNovelsByCategory(NovelSearchCondition condition, NovelPagingQuery pagingQuery, NovelCategory category);

    /**
     * 이번달 신작 소설 조회 메서드
     */
    List<? extends NovelQueryDto> findNewNovels(NovelSearchCondition condition, NovelPagingQuery pagingQuery);
}
