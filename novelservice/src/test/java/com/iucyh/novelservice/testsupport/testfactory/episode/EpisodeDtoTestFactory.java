package com.iucyh.novelservice.testsupport.testfactory.episode;

import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;

public class EpisodeDtoTestFactory {

    private EpisodeDtoTestFactory() {}

    public static CreateEpisodeRequest defaultCreateEpisodeRequest() {
        return new CreateEpisodeRequest(
                "test",
                "test desc",
                "test content"
        );
    }
}
