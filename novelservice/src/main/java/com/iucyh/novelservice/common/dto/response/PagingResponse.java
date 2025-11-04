package com.iucyh.novelservice.common.dto.response;

import java.util.List;

public record PagingResponse<T>(

        long total,
        Object next,
        List<T> items
) {}
