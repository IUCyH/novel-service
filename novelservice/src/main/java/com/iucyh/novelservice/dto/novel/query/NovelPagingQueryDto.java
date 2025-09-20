package com.iucyh.novelservice.dto.novel.query;

import com.iucyh.novelservice.domain.novel.Novel;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class NovelPagingQueryDto {

    private final Novel novel;
    private final Long viewAggId;

    @QueryProjection
    public NovelPagingQueryDto(Novel novel, Long viewAggId) {
        this.novel = novel;
        this.viewAggId = viewAggId;
    }
}
