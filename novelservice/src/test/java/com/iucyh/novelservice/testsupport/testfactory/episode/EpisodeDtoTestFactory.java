package com.iucyh.novelservice.testsupport.testfactory.episode;

import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.request.EpisodePagingRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeDetailRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;

import java.time.LocalDateTime;

public class EpisodeDtoTestFactory {

    private EpisodeDtoTestFactory() {}

    public static EpisodeResponse defaultEpisodeResponse() {
        return new EpisodeResponse(
                1L,
                "test",
                "test desc",
                1,
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static CreateEpisodeRequest defaultCreateEpisodeRequest() {
        return new CreateEpisodeRequest(
                "test",
                "test desc",
                "test content"
        );
    }

    public static UpdateEpisodeRequest defaultUpdateEpisodeRequest() {
        return new UpdateEpisodeRequest(
                "new",
                "new desc"
        );
    }

    public static UpdateEpisodeDetailRequest defaultUpdateEpisodeDetailRequest() {
        return new UpdateEpisodeDetailRequest(
                "new content"
        );
    }

    public static EpisodePagingRequest defaultEpisodePagingRequest() {
        return new EpisodePagingRequest(
                10,
                10
        );
    }
}
