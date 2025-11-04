package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.common.validator.enumfield.EnumField;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.iucyh.novelservice.common.constant.NovelConstants.*;

@Getter
@NoArgsConstructor
public class CreateNovelDto {

    @NotBlank
    @Length(min = NOVEL_TITLE_LENGTH_MIN, max = NOVEL_TITLE_LENGTH_MAX)
    private String title;

    @NotNull
    @Length(max = NOVEL_DESC_LENGTH_MAX)
    private String description;

    @NotNull
    @EnumField(enumClass = NovelCategory.class)
    private String category;

    public CreateNovelDto(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }
}
