package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.common.validator.enumfield.EnumField;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class NovelPagingRequestDto {

    @EnumField(enumClass = NovelSortType.class)
    private String sort = NovelSortType.POPULAR.getValue();

    @Size(max = 2048, message = "Cursor length is too long")
    private String cursor;

    @Range(min = 1, max = 100, message = "Limit must be between 1 and 100")
    private Integer limit = 50;
}
