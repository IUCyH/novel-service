package com.iucyh.novelservice.testsupport.testfactory.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class EpisodeEntityTestFactory {

    private EpisodeEntityTestFactory() {}

    public static Episode defaultEpisode() {
        return Episode.of(
                "test",
                "test desc",
                "test content",
                1,
                NovelEntityTestFactory.defaultNovel()
        );
    }

    public static Episode defaultEpisodeWithId() {
        Episode episode = Episode.of(
                "test",
                "test desc",
                "test content",
                1,
                NovelEntityTestFactory.defaultNovelWithId()
        );
        fillId(episode);
        return episode;
    }

    private static void fillId(Episode episode) {
        ReflectionTestUtils.setField(episode, "id", 1L);
    }
}
