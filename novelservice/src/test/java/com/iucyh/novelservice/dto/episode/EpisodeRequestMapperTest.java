package com.iucyh.novelservice.dto.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.episode.mapper.EpisodeRequestMapper;
import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeDtoTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class EpisodeRequestMapperTest {

    @Test
    @DisplayName("CreateEpisodeRequest -> Episode 변환 시 올바르게 매핑된다")
    void toEpisodeEntityMappedCorrectly() {
        // given
        CreateEpisodeRequest request = EpisodeDtoTestFactory.defaultCreateEpisodeRequest();
        Novel novel = NovelEntityTestFactory.defaultNovel();

        // when
        Episode result = EpisodeRequestMapper.toEpisodeEntity(request, novel);

        // then
        assertThat(result.getTitle()).isEqualTo(request.title());
        assertThat(result.getDescription()).isEqualTo(request.description());
        assertThat(result.getContent()).isEqualTo(request.content());
        assertThat(result.getEpisodeNumber()).isEqualTo(request.episodeNumber());

        assertThat(result.getNovel()).isEqualTo(novel);
    }
}
