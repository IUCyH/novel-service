package com.iucyh.novelservice.repository.episode.query;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.episode.query.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.repository.episode.EpisodeRepository;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.testsupport.annotation.RepositoryTest;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeEntityTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@RepositoryTest
public class EpisodeQueryRepositoryTest {

    private static final int DUMMY_DATA_SIZE = 10;
    private static final int FETCH_LIMIT = 5;

    private final EntityManager em;
    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;
    private final EpisodeQueryRepository episodeQueryRepository;

    private List<Novel> savedNovels;
    private Map<Long, List<Episode>> savedEpisodes;

    @Autowired
    public EpisodeQueryRepositoryTest(EntityManager em, NovelRepository novelRepository, EpisodeRepository episodeRepository, EpisodeQueryRepository episodeQueryRepository) {
        this.em = em;
        this.novelRepository = novelRepository;
        this.episodeRepository = episodeRepository;
        this.episodeQueryRepository = episodeQueryRepository;
    }

    @BeforeEach
    void beforeEach() {
        createDummyData();
    }

    @Test
    @DisplayName("특정 소설의 에피소드 리스트 조회에서 첫 페이지(커서가 존재하지 않을 때) 결과가 올바르게 나온다")
    void resultCorrectWithoutCursorWhenFindEpisodes() {
        // given
        long novelId = savedNovels.get(0).getId();

        // when
        List<EpisodeSimpleQueryDto> episodes = episodeQueryRepository.findEpisodesByNovelId(novelId, null, FETCH_LIMIT);

        // then
        assertThat(episodes).hasSize(FETCH_LIMIT);
        assertResultOrder(episodes);
    }

    @Test
    @DisplayName("특정 소설의 에피소드 리스트 조회에서 다음 페이지(커서가 존재할 때) 결과가 올바르게 나온다")
    void resultCorrectWithCursorWhenFindEpisodes() {
        // given
        long novelId = savedNovels.get(0).getId();
        int lastEpisodeNumber = DUMMY_DATA_SIZE / 2;

        // when
        List<EpisodeSimpleQueryDto> episodes = episodeQueryRepository.findEpisodesByNovelId(novelId, lastEpisodeNumber, FETCH_LIMIT);

        // then
        assertThat(episodes)
                .as("DUMMY_DATA_SIZE(10) / 2 가 5 일때 그 다음 조회할 남은 데이터 개수는 4개이고, FETCH_LIMIT은 5이므로 조회된 결과 개수는 4가 되야한다")
                .hasSize(4);
        assertResultOrder(episodes);
    }
    
    @Test
    @DisplayName("특정 소설의 에피소드 리스트 조회에서 소설이 여러개 존재할 때 정확히 선택한 소설의 회차만 조회된다")
    void onlyFetchedInSameNovelWhenFindEpisodes() {
        // given
        long novelId = savedNovels.get(0).getId();
        
        // when
        List<EpisodeSimpleQueryDto> episodes = episodeQueryRepository.findEpisodesByNovelId(novelId, null, FETCH_LIMIT);
        
        // then
        List<Long> savedEpisodeIds = savedEpisodes.get(novelId).stream()
                .sorted(Comparator.comparing(Episode::getEpisodeNumber).reversed())
                .limit(FETCH_LIMIT)
                .map(Episode::getId)
                .toList();
        List<Long> episodeIds = episodes.stream()
                .map(EpisodeSimpleQueryDto::getId)
                .toList();

        assertThat(episodeIds)
                .containsExactlyElementsOf(savedEpisodeIds);
    }

    @Test
    @DisplayName("특정 소설의 에피소드 리스트 조회에서 특정 회차가 삭제되었을 때 그 회차를 제외하고 조회한다")
    void excludeDeletedEpisodeWhenFindEpisodes() {
        // given
        long novelId = savedNovels.get(0).getId();
        Episode deletedEpisode = savedEpisodes.get(novelId).get(DUMMY_DATA_SIZE / 2);

        deletedEpisode.softDelete();
        em.merge(deletedEpisode);
        em.flush();
        em.clear();

        // when
        // limit 으로 인해 삭제된 에피소드가 애초부터 포함되지 않을 수 있으므로 전체 데이터 개수로 limit 설정
        List<EpisodeSimpleQueryDto> episodes = episodeQueryRepository.findEpisodesByNovelId(novelId, null, DUMMY_DATA_SIZE);

        // then
        List<Long> episodeIds = episodes.stream()
                .map(EpisodeSimpleQueryDto::getId)
                .toList();
        assertThat(episodeIds)
                .doesNotContain(deletedEpisode.getId());
    }

    private void createDummyData() {
        Novel novel1 = NovelEntityTestFactory.defaultNovel();
        Novel novel2 = NovelEntityTestFactory.defaultNovel();

        List<Episode> episodes = new ArrayList<>();

        for (int i = 0; i < DUMMY_DATA_SIZE; i++) {
            int sequence = i + 1;

            Episode episode1 = EpisodeEntityTestFactory.defaultEpisode(novel1, sequence);
            Episode episode2 = EpisodeEntityTestFactory.defaultEpisode(novel2, sequence);

            episodes.add(episode1);
            episodes.add(episode2);
        }

        savedNovels = novelRepository.saveAll(List.of(novel1, novel2));
        savedEpisodes = episodeRepository.saveAll(episodes).stream()
                .collect(
                        Collectors.groupingBy(e -> e.getNovel().getId())
                );

        em.flush();
        em.clear();
    }

    private void assertResultOrder(List<EpisodeSimpleQueryDto> episodes) {
        List<Integer> episodeNumbers = episodes.stream()
                .map(EpisodeSimpleQueryDto::getEpisodeNumber)
                .toList();
        assertThat(episodeNumbers)
                .isSortedAccordingTo(Comparator.reverseOrder());
    }
}
