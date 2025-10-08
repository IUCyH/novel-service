package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.common.validator.enumfield.EnumField;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import static com.iucyh.novelservice.common.constant.NovelConstants.*;

@Getter
public class UpdateNovelDto {

    @Size(min = NOVEL_TITLE_LENGTH_MIN, max = NOVEL_TITLE_LENGTH_MAX)
    private String title;

    @Size(max = NOVEL_DESC_LENGTH_MAX)
    private String description;

    @EnumField(enumClass = NovelCategory.class)
    private String category;
}
