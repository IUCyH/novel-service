package com.iucyh.novelservice.novel.web.dto.response;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;

import java.time.LocalDateTime;

public record NovelResponse(

        long novelId,
        String title,
        String description,
        int likeCount,
        int totalViewCount,
        NovelCategory category,
        LocalDateTime lastUpdateDate,
        LocalDateTime createdAt
) {}
