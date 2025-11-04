package com.iucyh.novelservice.episode.repository.query;

import com.iucyh.novelservice.episode.repository.query.condition.EpisodeSearchCondition;
import com.iucyh.novelservice.episode.repository.query.dto.EpisodeSimpleQueryDto;

import java.util.List;

public interface EpisodeQueryRepository {

    List<EpisodeSimpleQueryDto> findEpisodesByNovelId(long novelId, EpisodeSearchCondition condition);
}
