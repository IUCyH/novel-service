package com.iucyh.novelservice.common.exception.episode;

import com.iucyh.novelservice.common.exception.ServiceException;

import java.util.Map;

public class EpisodeNotFound extends ServiceException {

    public EpisodeNotFound(long episodeId) {
        super(
                EpisodeErrorCode.EPISODE_NOT_FOUND,
                Map.of("episodeId", episodeId)
        );
    }
}
