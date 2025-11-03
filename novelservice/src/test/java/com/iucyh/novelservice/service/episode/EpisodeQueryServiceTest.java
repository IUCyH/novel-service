package com.iucyh.novelservice.service.episode;

import com.iucyh.novelservice.common.exception.episode.EpisodeNotFound;
import com.iucyh.novelservice.common.exception.novel.NovelNotFound;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.episode.query.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.dto.episode.request.EpisodePagingRequest;
import com.iucyh.novelservice.dto.episode.response.EpisodeDetailResponse;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;
import com.iucyh.novelservice.repository.episode.EpisodeRepository;
import com.iucyh.novelservice.repository.episode.projection.EpisodeDetail;
import com.iucyh.novelservice.repository.episode.query.EpisodeQueryRepository;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.service.novel.NovelViewCountService;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeDtoTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeEntityTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EpisodeQueryServiceTest {

    @Mock
    private NovelViewCountService viewCountService;

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private EpisodeRepository episodeRepository;

    @Mock
    private EpisodeQueryRepository episodeQueryRepository;

    @InjectMocks
    private EpisodeQueryService episodeQueryService;
    
    @Test
    @DisplayName("특정 소설의 회차 조회에서 존재하지 않는 소설이라면 도메인 예외가 발생한다")
    void failedFindEpisodesByNovelWhenNovelDoesntExists() {
        // given
        long novelId = 1L;
        EpisodePagingRequest request = EpisodeDtoTestFactory.defaultEpisodePagingRequest();

        when(novelRepository.existsById(novelId))
                .thenReturn(false);
        
        // when
        Throwable throwable = catchThrowable(
                () -> episodeQueryService.findEpisodesByNovel(novelId, request)
        );

        // then
        assertThat(throwable)
                .isInstanceOf(NovelNotFound.class);
    }
    
    @Test
    @DisplayName("특정 소설의 회차 조회에서 쿼리 결과가 비어있다면 그 즉시 커서가 없는 결과를 반환한다")
    void findEpisodesByNovelReturnsEmptyWhenQueryResultEmpty() {
        // given
        long novelId = 1L;
        EpisodePagingRequest request = EpisodeDtoTestFactory.defaultEpisodePagingRequest();

        when(novelRepository.existsById(novelId))
                .thenReturn(true);

        when(episodeQueryRepository.findEpisodesByNovelId(novelId, request.lastEpisode(), request.limit()))
                .thenReturn(List.of());
        
        // when
        PagingResultDto<EpisodeResponse> result = episodeQueryService.findEpisodesByNovel(novelId, request);

        // then
        assertThat(result.getItems())
                .isEmpty();
        assertThat(result.getNextCursor())
                .isNull();
    }
    
    @Test
    @DisplayName("특정 소설의 회차 조회에서 쿼리 결과가 존재한다면 정상적인 결과를 반환한다")
    void findEpisodesByNovelReturnsValidDto() {
        // given
        long novelId = 1L;
        EpisodePagingRequest request = EpisodeDtoTestFactory.defaultEpisodePagingRequest();

        EpisodeSimpleQueryDto dummyQueryDto1 = EpisodeEntityTestFactory.defaultEpisodeSimpleQueryDto(1);
        EpisodeSimpleQueryDto dummyQueryDto2 = EpisodeEntityTestFactory.defaultEpisodeSimpleQueryDto(2);

        when(novelRepository.existsById(novelId))
                .thenReturn(true);

        when(episodeQueryRepository.findEpisodesByNovelId(novelId, request.lastEpisode(), request.limit()))
                .thenReturn(List.of(dummyQueryDto1, dummyQueryDto2));
        
        // when
        PagingResultDto<EpisodeResponse> result = episodeQueryService.findEpisodesByNovel(novelId, request);
        
        // then
        assertThat(result.getItems())
                .hasSize(2);

        assertThat(result.getNextCursor())
                .isEqualTo(dummyQueryDto2.getEpisodeNumber());
    }

    @Test
    @DisplayName("특정 소설의 상세 정보 조회에서 조회수 증가가 실패하면 로그를 남기고 무시한다")
    void findEpisodeDetailIgnoresFailureToIncreaseViewCounts() {
        // given
        long novelId = 1L;
        int episodeNumber = 1;
        EpisodeDetail episodeDetail = mockEpisodeDetail("content");

        when(episodeRepository.findEpisodeDetail(novelId, episodeNumber))
                .thenReturn(Optional.of(episodeDetail));

        doThrow(new DataAccessException("test exception") {})
                .when(viewCountService).increaseViewCounts(anyLong(), anyLong());

        // when
        EpisodeDetailResponse result = episodeQueryService.findEpisodeDetail(novelId, episodeNumber);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("특정 소설의 상세 정보 조회에서 쿼리 결과가 Empty 라면 도메인 예외가 발생한다")
    void failedFindEpisodeDetailWhenQueryResultEmpty() {
        // given
        long novelId = 1L;
        int episodeNumber = 1;

        when(episodeRepository.findEpisodeDetail(novelId, episodeNumber))
                .thenReturn(Optional.empty());

        // when
        Throwable throwable = catchThrowable(
                () -> episodeQueryService.findEpisodeDetail(novelId, episodeNumber)
        );

        // then
        assertThat(throwable)
                .isInstanceOf(EpisodeNotFound.class);

        EpisodeNotFound episodeNotFound = (EpisodeNotFound) throwable;
        EpisodeNotFound tempEpisodeNotFound = EpisodeNotFound.withEpisodeNumber(episodeNumber);

        assertThat(episodeNotFound.getCauses())
                .isEqualTo(tempEpisodeNotFound.getCauses());
    }

    @Test
    @DisplayName("특정 소설의 상세 정보 조회에서 결과가 올바르게 나온다")
    void successFindEpisodeDetail() {
        // given
        long novelId = 1L;
        int episodeNumber = 1;
        String detailContent = "content";
        EpisodeDetail episodeDetail = mockEpisodeDetail(detailContent);

        when(episodeRepository.findEpisodeDetail(novelId, episodeNumber))
                .thenReturn(Optional.of(episodeDetail));

        // when
        EpisodeDetailResponse result = episodeQueryService.findEpisodeDetail(novelId, episodeNumber);

        // then
        assertThat(result)
                .isNotNull();
        assertThat(result.content())
                .isEqualTo(detailContent);
    }

    private EpisodeDetail mockEpisodeDetail(String detailContent) {
        EpisodeDetail episodeDetail = Mockito.mock(EpisodeDetail.class);
        when(episodeDetail.getContent())
                .thenReturn(detailContent);

        return episodeDetail;
    }
}
