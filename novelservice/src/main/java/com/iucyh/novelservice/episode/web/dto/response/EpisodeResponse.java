package com.iucyh.novelservice.episode.web.dto.response;

import java.time.LocalDateTime;

public record EpisodeResponse(

        long episodeId,
        String title,
        String description,
        int episodeNumber,
        int viewCount,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {}
