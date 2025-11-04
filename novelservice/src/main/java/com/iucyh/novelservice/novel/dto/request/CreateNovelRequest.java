package com.iucyh.novelservice.novel.dto.request;

import com.iucyh.novelservice.common.validator.enumfield.EnumField;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.iucyh.novelservice.novel.constant.NovelConstants.*;

public record CreateNovelRequest(

        @NotBlank
        @Size(min = NOVEL_TITLE_LENGTH_MIN, max = NOVEL_TITLE_LENGTH_MAX)
        String title,

        @NotNull
        @Size(max = NOVEL_DESC_LENGTH_MAX)
        String description,

        @NotNull
        @EnumField(enumClass = NovelCategory.class)
        String category
) {}
