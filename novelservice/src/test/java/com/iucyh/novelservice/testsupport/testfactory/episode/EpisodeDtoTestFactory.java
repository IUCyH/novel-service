package com.iucyh.novelservice.testsupport.testfactory.episode;

import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeDetailRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeRequest;

public class EpisodeDtoTestFactory {

    private EpisodeDtoTestFactory() {}

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
}
