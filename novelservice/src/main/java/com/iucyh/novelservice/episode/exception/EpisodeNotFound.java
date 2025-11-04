package com.iucyh.novelservice.episode.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.episode.exception.errorcode.EpisodeErrorCode;

import java.util.Map;

public class EpisodeNotFound extends ServiceException {

    public EpisodeNotFound(long episodeId) {
        super(
                EpisodeErrorCode.EPISODE_NOT_FOUND,
                Map.of("episodeId", episodeId)
        );
    }

    private <T> EpisodeNotFound(String message, String fieldName, T value) {
        super(
                EpisodeErrorCode.EPISODE_NOT_FOUND,
                message,
                Map.of(fieldName, value)
        );
    }

    public static EpisodeNotFound withEpisodeNumber(int episodeNumber) {
        return new EpisodeNotFound(
                "Episode not found with this episode number",
                "episodeNumber",
                episodeNumber
        );
    }
}
