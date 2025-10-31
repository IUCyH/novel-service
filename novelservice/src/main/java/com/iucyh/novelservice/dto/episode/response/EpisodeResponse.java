package com.iucyh.novelservice.dto.episode.response;

public record EpisodeResponse(

        long episodeId,
        String title,
        String description,
        int episodeNumber,
        int viewCount
) {}
