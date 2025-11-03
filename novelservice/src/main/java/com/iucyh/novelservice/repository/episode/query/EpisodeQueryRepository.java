package com.iucyh.novelservice.repository.episode.query;

import com.iucyh.novelservice.dto.episode.query.EpisodeSimpleQueryDto;

import java.util.List;

public interface EpisodeQueryRepository {

    List<EpisodeSimpleQueryDto> findEpisodesByNovelId(long novelId, Integer lastEpisodeNumber, int limit);
}
