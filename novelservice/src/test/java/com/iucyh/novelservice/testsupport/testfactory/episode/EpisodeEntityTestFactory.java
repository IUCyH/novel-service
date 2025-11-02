package com.iucyh.novelservice.testsupport.testfactory.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.episode.query.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

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

    public static Episode defaultEpisode(Novel novel, int episodeNumber) {
        return Episode.of(
                "test",
                "test desc",
                "test content",
                episodeNumber,
                novel
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

    public static EpisodeSimpleQueryDto defaultEpisodeSimpleQueryDto() {
        return new EpisodeSimpleQueryDto(
                1L,
                "test",
                "test desc",
                1,
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static EpisodeSimpleQueryDto defaultEpisodeSimpleQueryDto(int episodeNumber) {
        return new EpisodeSimpleQueryDto(
                1L,
                "test",
                "test desc",
                episodeNumber,
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static void fillId(Episode episode) {
        ReflectionTestUtils.setField(episode, "id", 1L);
    }
}
