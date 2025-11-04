package com.iucyh.novelservice.episode.web.dto.mapper;

import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.episode.web.dto.request.CreateEpisodeRequest;

public class EpisodeRequestMapper {

    private EpisodeRequestMapper() {}

    public static Episode toEpisode(CreateEpisodeRequest request, Novel novel, int episodeNumber) {
        return Episode.of(
                request.title(),
                request.description(),
                request.content(),
                episodeNumber,
                novel
        );
    }
}
