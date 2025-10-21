package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.common.validator.enumfield.EnumField;
import com.iucyh.novelservice.common.validator.notblank.NotBlankIfPresent;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.iucyh.novelservice.common.constant.NovelConstants.*;

@Getter
@NoArgsConstructor
public class UpdateNovelDto {

    @NotBlankIfPresent
    @Size(min = NOVEL_TITLE_LENGTH_MIN, max = NOVEL_TITLE_LENGTH_MAX)
    private String title;

    @Size(max = NOVEL_DESC_LENGTH_MAX)
    private String description;

    @EnumField(enumClass = NovelCategory.class)
    private String category;

    public UpdateNovelDto(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }
}
