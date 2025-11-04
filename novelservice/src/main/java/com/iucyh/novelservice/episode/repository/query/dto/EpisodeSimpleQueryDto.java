package com.iucyh.novelservice.episode.repository.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EpisodeSimpleQueryDto {

    private final Long id;
    private final String title;
    private final String description;
    private final Integer episodeNumber;
    private final Integer viewCount;
    private final LocalDateTime updatedAt;
    private final LocalDateTime createdAt;

    @QueryProjection
    public EpisodeSimpleQueryDto(Long id, String title, String description, Integer episodeNumber, Integer viewCount, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.episodeNumber = episodeNumber;
        this.viewCount = viewCount;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
