package com.iucyh.novelservice.novel.repository.query.dto;

import com.iucyh.novelservice.novel.domain.Novel;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class NovelPopularQueryDto implements NovelQueryDto {

    private final Novel novel;
    private final long lastAggId;
    private final int lastAggViewCount;

    @QueryProjection
    public NovelPopularQueryDto(Novel novel, long lastAggId, int lastAggViewCount) {
        this.novel = novel;
        this.lastAggId = lastAggId;
        this.lastAggViewCount = lastAggViewCount;
    }
}
