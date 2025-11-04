package com.iucyh.novelservice.novel.web.dto.request;

import com.iucyh.novelservice.common.validator.enumfield.EnumField;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record NovelPagingRequest(

        @EnumField(enumClass = NovelSortType.class)
        String sort,

        @Size(max = 2048, message = "Cursor length is too long")
        String cursor,

        @Range(min = 1, max = 100, message = "Limit must be between 1 and 100")
        Integer limit
) {
    public NovelPagingRequest {
        if (sort == null) {
            sort = NovelSortType.POPULAR.getValue();
        }

        if (limit == null) {
            limit = 50;
        }
    }
}
