package com.iucyh.novelservice.repository.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.testsupport.annotation.RepositoryTest;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeEntityTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RepositoryTest
public class EpisodeRepositoryTest {

    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;
    private final EntityManager em;

    @Autowired
    public EpisodeRepositoryTest(NovelRepository novelRepository, EpisodeRepository episodeRepository, EntityManager em) {
        this.novelRepository = novelRepository;
        this.episodeRepository = episodeRepository;
        this.em = em;
    }
    
    @Test
    @DisplayName("특정 소설에 등록된 회차가 없다면 마지막 회차 번호 조회가 Optional.empty를 반환한다")
    void findMaxEpisodeNumberWhenNoEpisodesInNovel() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovel();
        novelRepository.save(novel);
        
        // when
        Optional<Integer> lastEpisodeNumber = episodeRepository.findLastEpisodeNumber(novel.getId());
        
        // then
        assertThat(lastEpisodeNumber).isEmpty();
    }

    @Test
    @DisplayName("여러 소설이 존재할 때 선택한 소설의 에피소드 중에서만 마지막 회차 번호를 조회한다")
    void findLastEpisodeNumberWhenMultipleNovelsExist() {
        // given
        Novel novel1 = NovelEntityTestFactory.defaultNovel();
        Novel novel2 = NovelEntityTestFactory.defaultNovel();

        Episode firstEpisodeWithNovel1 = EpisodeEntityTestFactory.defaultEpisode(novel1, 1);
        Episode secondEpisodeWithNovel1 = EpisodeEntityTestFactory.defaultEpisode(novel1, 2);

        Episode firstEpisodeWithNovel2 = EpisodeEntityTestFactory.defaultEpisode(novel2, 1);

        saveMultipleDummyData(novel1, List.of(firstEpisodeWithNovel1, secondEpisodeWithNovel1));
        saveDummyData(firstEpisodeWithNovel2);

        // when
        Optional<Integer> lastEpisodeNumber = episodeRepository.findLastEpisodeNumber(novel1.getId());

        // then
        assertThat(lastEpisodeNumber).isPresent();
        assertThat(lastEpisodeNumber.get()).isEqualTo(secondEpisodeWithNovel1.getEpisodeNumber());
    }

    @Test
    @DisplayName("특정 소설에 등록된 회차가 1개일 때 마지막 회차 번호 조회가 성공한다")
    void findMaxEpisodeNumberWithOneEpisode() {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisode();
        Novel novel = episode.getNovel();

        saveDummyData(episode);

        // when
        Optional<Integer> lastEpisodeNumber = episodeRepository.findLastEpisodeNumber(novel.getId());

        // then
        assertThat(lastEpisodeNumber).isPresent();
        assertThat(lastEpisodeNumber.get()).isEqualTo(episode.getEpisodeNumber());
    }

    @Test
    @DisplayName("특정 소설에 등록된 회차가 2개 이상일 때 마지막 회차 번호 조회가 성공한다")
    void findMaxEpisodeNumberWithMultipleEpisodes() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovel();
        Episode episode1 = EpisodeEntityTestFactory.defaultEpisode(novel, 1);
        Episode episode3 = EpisodeEntityTestFactory.defaultEpisode(novel, 3);
        Episode episode2 = EpisodeEntityTestFactory.defaultEpisode(novel, 2);

        saveMultipleDummyData(novel, List.of(episode1, episode3, episode2));

        // when
        Optional<Integer> lastEpisodeNumber = episodeRepository.findLastEpisodeNumber(novel.getId());

        // then
        assertThat(lastEpisodeNumber).isPresent();
        assertThat(lastEpisodeNumber.get()).isEqualTo(episode3.getEpisodeNumber());
    }
    
    @Test
    @DisplayName("소설 id + 회차 id 조합 회차 조회시 회차가 존재하지 않는다면 실패한다")
    void failedFindByIdAndNovelIdWhenNoEpisodeExists() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovel();
        long notExistsEpisodeId = 1L;

        novelRepository.save(novel);
        
        // when
        Optional<Episode> result = episodeRepository.findByIdAndNovelId(notExistsEpisodeId, novel.getId());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("소설 id + 회차 id 조합 회차 조회시 소설이 존재하지 않는다면 실패한다")
    void failedFindByIdAndNovelIdWhenNoNovelExists() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovel();
        Episode episode = EpisodeEntityTestFactory.defaultEpisode(novel, 1);
        long notExistsNovelId = 999L;

        saveDummyData(episode);

        // when
        Optional<Episode> result = episodeRepository.findByIdAndNovelId(episode.getId(), notExistsNovelId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("소설 id + 회차 id 조합 회차 조회시 삭제된 회차라면 실패한다")
    void failedFindByIdAndNovelIdWhenEpisodeIsDeleted() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovel();
        Episode episode = EpisodeEntityTestFactory.defaultEpisode(novel, 1);

        episode.softDelete();
        saveDummyData(episode);

        // when
        Optional<Episode> result = episodeRepository.findByIdAndNovelId(episode.getId(), novel.getId());

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("소설 id + 회차 id 조합 회차 조회시 모든 조건이 정상이라면 성공한다")
    void successFindByIdAndNovelId() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovel();
        Episode episode = EpisodeEntityTestFactory.defaultEpisode(novel, 1);

        saveDummyData(episode);

        // when
        Optional<Episode> result = episodeRepository.findByIdAndNovelId(episode.getId(), novel.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(episode.getId());
        assertThat(result.get().getTitle()).isEqualTo(episode.getTitle());
    }

    private void saveDummyData(Episode episode) {
        novelRepository.save(episode.getNovel());
        episodeRepository.save(episode);

        em.flush();
        em.clear();
    }

    private void saveMultipleDummyData(Novel novel, List<Episode> episodes) {
        novelRepository.save(novel);
        episodeRepository.saveAll(episodes);

        em.flush();
        em.clear();
    }
}
