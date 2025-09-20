package com.iucyh.novelservice.repository.novel.query;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;

import java.util.List;

public interface NovelQueryRepository {

    List<NovelPagingQueryDto> findNovels(NovelSearchCondition condition, int limit);
    List<NovelPagingQueryDto> findNovelsByCategory(NovelSearchCondition condition, NovelCategory category, int limit);

    /**
     * 이번달 신작 소설 조회 메서드
     */
    List<NovelPagingQueryDto> findNewNovels(NovelSearchCondition condition, int limit);
}
