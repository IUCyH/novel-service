package com.iucyh.novelservice.novel.repository.query.dto;

import com.iucyh.novelservice.novel.domain.Novel;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class NovelSimpleQueryDto implements NovelQueryDto {

    private final Novel novel;

    @QueryProjection
    public NovelSimpleQueryDto(Novel novel) {
        this.novel = novel;
    }
}
