package com.iucyh.novelservice.dto.episode.mapper;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.dto.episode.response.EpisodeDetailResponse;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;

public class EpisodeResponseMapper {

    private EpisodeResponseMapper() {}

    public static EpisodeResponse toEpisodeResponse(Episode episode) {
        return new EpisodeResponse(
                episode.getId(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getEpisodeNumber(),
                episode.getViewCount()
        );
    }

    public static EpisodeDetailResponse toEpisodeDetailResponse(Episode episode) {
        return new EpisodeDetailResponse(episode.getContent());
    }
}
