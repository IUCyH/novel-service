package com.iucyh.novelservice.episode.repository.query.condition;

public record EpisodeSearchCondition(

        Integer lastEpisodeNumber,
        int limit
) {}
