package com.iucyh.novelservice.dto.episode.mapper;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;

public class EpisodeRequestMapper {

    private EpisodeRequestMapper() {}

    public static Episode toEpisodeEntity(CreateEpisodeRequest request, Novel novel) {
        return Episode.of(
                request.title(),
                request.description(),
                request.content(),
                request.episodeNumber(),
                novel
        );
    }
}
