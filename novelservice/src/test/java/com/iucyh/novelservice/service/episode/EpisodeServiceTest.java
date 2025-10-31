package com.iucyh.novelservice.service.episode;

import com.iucyh.novelservice.common.exception.novel.NovelErrorCode;
import com.iucyh.novelservice.common.exception.novel.NovelNotFound;
import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;
import com.iucyh.novelservice.repository.episode.EpisodeRepository;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeDtoTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeEntityTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EpisodeServiceTest {

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private EpisodeRepository episodeRepository;

    @InjectMocks
    private EpisodeService episodeService;

    @Test
    @DisplayName("novelId 에 해당하는 소설을 찾지 못하면 Novel 도메인 예외를 던진다")
    void failedFindNovel() {
        // given
        long novelId = 1L;
        CreateEpisodeRequest request = EpisodeDtoTestFactory.defaultCreateEpisodeRequest();

        when(novelRepository.findById(novelId))
                .thenReturn(Optional.empty());

        // when
        Throwable throwable = catchThrowable(() -> episodeService.createEpisode(novelId, request));

        // then
        assertThat(throwable)
                .isInstanceOf(NovelNotFound.class);

        NovelNotFound novelNotFound = (NovelNotFound) throwable;
        assertThat(novelNotFound.getErrorCode())
                .isEqualTo(NovelErrorCode.NOVEL_NOT_FOUND);
    }

    @Test
    @DisplayName("회차 등록에서 마지막 회차 번호 조회 결과가 empty 라도 정상적으로 1번으로 등록된다")
    void createEpisodeSuccessWhenNoEpisodesInNovel() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovelWithId();
        CreateEpisodeRequest request = EpisodeDtoTestFactory.defaultCreateEpisodeRequest();

        when(novelRepository.findById(novel.getId()))
                .thenReturn(Optional.of(novel));

        when(episodeRepository.findLastEpisodeNumber(novel.getId()))
                .thenReturn(Optional.empty());

        mockSaveEpisode();

        // when
        EpisodeResponse result = episodeService.createEpisode(novel.getId(), request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.episodeNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("마지막 회차가 존재할 경우 로직이 모두 정상적으로 처리되어 등록된다")
    void createEpisodeSuccessWhenLastEpisodeExists() {
        // given
        Episode lastEpisode = EpisodeEntityTestFactory.defaultEpisodeWithId();
        Novel novel = lastEpisode.getNovel();
        CreateEpisodeRequest request = EpisodeDtoTestFactory.defaultCreateEpisodeRequest();

        when(novelRepository.findById(novel.getId()))
                .thenReturn(Optional.of(novel));

        when(episodeRepository.findLastEpisodeNumber(novel.getId()))
                .thenReturn(Optional.of(lastEpisode.getEpisodeNumber()));

        mockSaveEpisode();

        // when
        EpisodeResponse result = episodeService.createEpisode(novel.getId(), request);

        // then
        int nextEpisodeNumber = lastEpisode.getEpisodeNumber() + 1;

        assertThat(result).isNotNull();
        assertThat(result.episodeNumber()).isEqualTo(nextEpisodeNumber);
        assertThat(novel.getLastEpisodeAt()).isEqualTo(result.createdAt());
    }

    private void mockSaveEpisode() {
        doAnswer((answer) -> {
            Object[] args = answer.getArguments();
            Episode episode = (Episode) args[0];
            ReflectionTestUtils.setField(episode, "id", 1L);
            ReflectionTestUtils.setField(episode, "createdAt", LocalDateTime.now());

            return episode;
        }).when(episodeRepository).save(any());
    }
}
