package com.iucyh.novelservice.episode.web.dto.mapper;

import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.common.dto.response.PagingResponse;
import com.iucyh.novelservice.episode.repository.query.dto.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeResponse;
import com.iucyh.novelservice.episode.repository.projection.EpisodeDetail;

import java.util.List;

public class EpisodeResponseMapper {

    private EpisodeResponseMapper() {}

    public static EpisodeResponse toEpisodeResponse(Episode episode) {
        return new EpisodeResponse(
                episode.getId(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getEpisodeNumber(),
                episode.getViewCount(),
                episode.getUpdatedAt(),
                episode.getCreatedAt()
        );
    }

    public static EpisodeResponse toEpisodeResponse(EpisodeSimpleQueryDto episode) {
        return new EpisodeResponse(
                episode.getId(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getEpisodeNumber(),
                episode.getViewCount(),
                episode.getUpdatedAt(),
                episode.getCreatedAt()
        );
    }

    public static EpisodeDetailResponse toEpisodeDetailResponse(Episode episode) {
        return new EpisodeDetailResponse(episode.getContent());
    }

    public static EpisodeDetailResponse toEpisodeDetailResponse(EpisodeDetail episodeDetail) {
        return new EpisodeDetailResponse(episodeDetail.getContent());
    }

    public static PagingResponse<EpisodeResponse> toPagingResultDto(List<EpisodeResponse> episodes, long totalCount, Integer lastEpisodeNumber) {
        return new PagingResponse<>(totalCount, lastEpisodeNumber, episodes);
    }
}
