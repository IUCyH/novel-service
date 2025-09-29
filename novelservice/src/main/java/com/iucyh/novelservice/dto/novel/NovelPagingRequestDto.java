package com.iucyh.novelservice.dto.novel;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class NovelPagingRequestDto {

    private NovelSortType sort = NovelSortType.POPULAR;

    @Size(max = 2048)
    private String cursor;

    @Range(min = 1, max = 100)
    private Integer limit = 50;
}
