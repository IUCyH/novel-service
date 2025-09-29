package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NovelDto {

    private final String publicId;
    private final String title;
    private final String description;
    private final int likeCount;
    private final int totalViewCount;
    private final NovelCategory category;
    private final LocalDateTime lastUpdateDate;
    private final LocalDateTime createdAt;
}
