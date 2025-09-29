package com.iucyh.novelservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PagingResultDto<T> {

    private final long totalCount;
    private final String nextCursor;
    private final List<T> items;
}
