package com.iucyh.novelservice.dto.novel.query;

import com.iucyh.novelservice.domain.novel.Novel;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class NovelPopularQueryDto implements NovelPagingQueryDto {

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
