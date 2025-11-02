package com.iucyh.novelservice.dto.episode.mapper;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.episode.query.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.dto.episode.response.EpisodeDetailResponse;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;

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

    public static PagingResultDto<EpisodeResponse> toPagingResultDto(List<EpisodeResponse> episodes, long totalCount, Integer lastEpisodeNumber) {
        return new PagingResultDto<>(totalCount, lastEpisodeNumber, episodes);
    }
}
