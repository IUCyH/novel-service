package com.iucyh.novelservice.repository.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.repository.IdResult;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class EpisodeRepositoryTest {

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Test
    @DisplayName("Episode의 publicId로 id 조회 - 성공")
    void getIdByPublicIdWithSuccess() {
        // given
        Novel novel = Novel.of("test novel", "test object", NovelCategory.ETC);
        Episode episode = Episode.of("test episode", "test object", "content", 1, novel);

        novelRepository.save(novel);
        episodeRepository.save(episode);

        // when
        Optional<IdResult> idResult = episodeRepository.findIdByPublicId(episode.getPublicId());

        // then
        assertThat(idResult).isPresent();
        assertThat(idResult.get().getId()).isEqualTo(episode.getId());
    }

    @Test
    @DisplayName("Episode의 publicId로 id 조회 - 실패")
    void getIdByPublicIdWithFail() {
        // given
        Novel novel = Novel.of("test novel", "test object", NovelCategory.ETC);
        Episode episode = Episode.of("test episode", "test object", "content", 1, novel);

        novelRepository.save(novel);
        episodeRepository.save(episode);

        // when
        Optional<IdResult> idResult = episodeRepository.findIdByPublicId("unvalidpublicid");

        // then
        assertThat(idResult).isEmpty();
    }
}