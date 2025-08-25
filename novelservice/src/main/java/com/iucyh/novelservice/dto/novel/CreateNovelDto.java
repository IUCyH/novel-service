package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CreateNovelDto {

    @NotEmpty
    @Length(min = 1, max = 64)
    private String title;

    @NotEmpty
    @Length(max = 32)
    private String description;

    private NovelCategory category;
}
