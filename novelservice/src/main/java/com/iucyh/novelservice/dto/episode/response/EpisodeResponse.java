package com.iucyh.novelservice.dto.episode.response;

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
