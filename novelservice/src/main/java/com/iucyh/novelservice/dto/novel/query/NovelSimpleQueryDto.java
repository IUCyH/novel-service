package com.iucyh.novelservice.dto.novel.query;

import com.iucyh.novelservice.domain.novel.Novel;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class NovelSimpleQueryDto implements NovelPagingQueryDto {

    private final Novel novel;

    @QueryProjection
    public NovelSimpleQueryDto(Novel novel) {
        this.novel = novel;
    }
}
