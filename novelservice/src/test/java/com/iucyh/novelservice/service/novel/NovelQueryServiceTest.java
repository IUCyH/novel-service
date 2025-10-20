package com.iucyh.novelservice.service.novel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iucyh.novelservice.common.util.Base64Util;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.novel.NovelDto;
import com.iucyh.novelservice.dto.novel.NovelPagingRequestDto;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.NovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.repository.novel.query.NovelQueryRepository;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelPopularCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelLastUpdatePagingQuery;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelPopularPagingQuery;
import com.iucyh.novelservice.service.novel.codec.NovelCursorBase64Codec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NovelQueryServiceTest {

    @Mock
    private NovelCursorBase64Codec base64Codec;

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private NovelQueryRepository novelQueryRepository;

    @Mock
    private NovelPopularPagingQuery novelPopularPagingQuery;

    @Mock
    private NovelLastUpdatePagingQuery novelLastUpdatePagingQuery;

    private NovelQueryService novelQueryService;

    @BeforeEach
    void beforeEach() {
        when(novelPopularPagingQuery.getSupportedSortType())
                .thenReturn(NovelSortType.POPULAR);
        when(novelLastUpdatePagingQuery.getSupportedSortType())
                .thenReturn(NovelSortType.LAST_UPDATE);

        novelQueryService = new NovelQueryService(
                base64Codec,
                novelRepository,
                novelQueryRepository,
                List.of(novelPopularPagingQuery, novelLastUpdatePagingQuery)
        );
    }

    @Test
    @DisplayName("카테고리별 인기순 상위 10개 조회 시 기대값과 리스트 크기가 일치한다")
    void findNovelsByCategoryInSummarySuccess() {
        // given
        NovelCategory category = NovelCategory.ETC;
        Novel testNovel = Novel.of("test", "", category);

        List<NovelPagingQueryDto> expectedResult = List.of(new NovelSimpleQueryDto(testNovel));
        doReturn(expectedResult)
                .when(novelQueryRepository)
                .findNovelsByCategory(any(), eq(novelPopularPagingQuery), eq(category), eq(10));

        // when
        List<NovelDto> result = novelQueryService.findNovelsByCategoryInSummary(category);

        // then
        assertThat(result).hasSize(expectedResult.size());
    }

    @Test
    @DisplayName("이번달 신작 소설 상위 30개 조회 시 기대값과 리스트 크기가 일치한다")
    void findNewNovelsInSummarySuccess() {
        // given
        LocalDateTime now = LocalDateTime.now().withDayOfMonth(1);
        Novel testNovel = Novel.of("test", "", NovelCategory.ETC);
        testNovel.updateLastEpisodeAt(now);

        List<NovelPagingQueryDto> expectedResult = List.of(new NovelSimpleQueryDto(testNovel));
        doReturn(expectedResult)
                .when(novelQueryRepository)
                .findNewNovels(any(), eq(novelLastUpdatePagingQuery), eq(30));

        // when
        List<NovelDto> result = novelQueryService.findNewNovelsInSummary();

        // then
        assertThat(result).hasSize(expectedResult.size());
    }

    @Test
    @DisplayName("executePagingQuery 메서드에서 쿼리 결과가 비어있다면 그 즉시 커서가 없는 dto를 반환한다")
    void executePagingQueryReturnsEmptyDto() {
        // given
        NovelPagingRequestDto requestDto = new NovelPagingRequestDto();
        requestDto.setSort(NovelSortType.POPULAR.getValue());

        when(novelQueryRepository.findNovels(any(), eq(novelPopularPagingQuery), anyInt()))
                .thenReturn(List.of());

        // when
        PagingResultDto<NovelDto> result = novelQueryService.findNovels(requestDto);

        // then
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getNextCursor()).isNull();
    }

    @Test
    @DisplayName("executePagingQuery 메서드에서 쿼리 결과가 있다면 모든 필드가 정상적으로 채워진 dto를 반환한다")
    void executePagingQueryReturnsNotEmptyDto() {
        // given
        NovelPagingRequestDto requestDto = new NovelPagingRequestDto();
        requestDto.setSort(NovelSortType.POPULAR.getValue());

        Novel testNovel1 = Novel.of("test1", "", NovelCategory.ETC);
        Novel testNovel2 = Novel.of("test2", "", NovelCategory.ETC);
        List<NovelPagingQueryDto> expectedResult = List.of(
                new NovelSimpleQueryDto(testNovel1),
                new NovelSimpleQueryDto(testNovel2)
        );

        NovelPopularCursor testCursor = new NovelPopularCursor(1, 100);

        doReturn(expectedResult)
                .when(novelQueryRepository)
                .findNovels(any(), eq(novelPopularPagingQuery), anyInt());

        // 쿼리 결과의 마지막 값으로 커서 생성 메서드를 호출하면 testCursor 반환
        when(novelPopularPagingQuery.createCursor(
                eq(expectedResult.get(1))
        )).thenReturn(testCursor);

        // 테스트 용이성을 위해 인코딩 메서드 호출 시 testNovel2의 제목을 반환하도록 설정
        when(base64Codec.encode(any()))
                .thenReturn(testNovel2.getTitle());

        // when
        PagingResultDto<NovelDto> result = novelQueryService.findNovels(requestDto);

        // then
        assertThat(result.getItems()).hasSize(expectedResult.size());
        assertThat(result.getNextCursor()).isNotNull();
        assertThat(result.getNextCursor()).isEqualTo(testNovel2.getTitle());
    }
}
