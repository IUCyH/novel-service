package com.iucyh.novelservice.novel.repository.dto;

import com.iucyh.novelservice.novel.domain.Novel;
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
