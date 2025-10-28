package com.iucyh.novelservice.controller.novel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iucyh.novelservice.common.constant.NovelConstants;
import com.iucyh.novelservice.common.exception.CommonErrorCode;
import com.iucyh.novelservice.common.exception.ErrorCode;
import com.iucyh.novelservice.common.exception.novel.DuplicateNovelTitle;
import com.iucyh.novelservice.common.exception.novel.NovelErrorCode;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.novel.CreateNovelDto;
import com.iucyh.novelservice.dto.novel.NovelPagingRequestDto;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.iucyh.novelservice.dto.novel.UpdateNovelDto;
import com.iucyh.novelservice.service.novel.NovelQueryService;
import com.iucyh.novelservice.service.novel.NovelService;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelDtoTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NovelController.class)
@WithMockUser
public class NovelControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NovelService novelService;

    @MockitoBean
    private NovelQueryService novelQueryService;

    private static Stream<Arguments> createNovelInvalidCases() {
        String validTitle = "title";
        String validDesc = "desc";
        String validCategory = NovelCategory.ETC.getValue();

        return Stream.of(
                Arguments.of("title이 빈 값일 때", new CreateNovelDto("", validDesc, validCategory)),
                Arguments.of("title이 null 일 때", new CreateNovelDto(null, validDesc, validCategory)),
                Arguments.of("title의 길이가 최솟값보다 작을 때", new CreateNovelDto(" ", validDesc, validCategory)),
                Arguments.of("title의 길이가 최댓값보다 클 때", new CreateNovelDto("a".repeat(NovelConstants.NOVEL_TITLE_LENGTH_MAX + 1), validDesc, validCategory)),
                Arguments.of("description이 null 일 때", new CreateNovelDto(validTitle, null, validCategory)),
                Arguments.of("description의 길이가 최댓값보다 클 때", new CreateNovelDto(validTitle, "a".repeat(NovelConstants.NOVEL_DESC_LENGTH_MAX + 1), validCategory)),
                Arguments.of("category가 null 일 때", new CreateNovelDto(validTitle, validDesc, null)),
                Arguments.of("category가 잘못된 값일 때", new CreateNovelDto(validTitle, validDesc, "invalid"))
        );
    }

    private static Stream<Arguments> updateNovelInvalidCases() {
        String validTitle = "title";
        String validDesc = "desc";
        String validCategory = NovelCategory.ETC.getValue();

        return Stream.of(
                Arguments.of("title의 길이가 최솟값보다 작을 때", new UpdateNovelDto(" ", validDesc, validCategory)),
                Arguments.of("title의 길이가 최댓값보다 클 때", new UpdateNovelDto("a".repeat(NovelConstants.NOVEL_TITLE_LENGTH_MAX + 1), validDesc, validCategory)),
                Arguments.of("description의 길이가 최댓값보다 클 때", new UpdateNovelDto(validTitle, "a".repeat(NovelConstants.NOVEL_DESC_LENGTH_MAX + 1), validCategory)),
                Arguments.of("category가 잘못된 값일 때", new UpdateNovelDto(validTitle, validDesc, "invalid"))
        );
    }

    @Test
    @DisplayName("소설 전체 리스트 조회 에서 잘못된 sort 타입이 전달되면 검증 에러가 발생한다")
    void failedGetNovelsWithInvalidSortType() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/novels")
                        .param("sort", "invalid")
        );

        // then
        ResultMatcher[] commonResultMatchers = buildFailTestCommonResultMatchers(CommonErrorCode.VALIDATION_FAILED);
        action
                .andExpect(status().isBadRequest())
                .andExpectAll(commonResultMatchers);
    }

    @Test
    @DisplayName("소설 전체 리스트 조회 에서 limit 이 최대값을 넘으면 검증 에러가 발생한다")
    void failedGetNovelsWithInvalidLimit() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/novels")
                        .param("limit", "1000")
        );

        // then
        ResultMatcher[] commonResultMatchers = buildFailTestCommonResultMatchers(CommonErrorCode.VALIDATION_FAILED);
        action
                .andExpect(status().isBadRequest())
                .andExpectAll(commonResultMatchers);
    }

    @Test
    @DisplayName("소설 전체 리스트 조회 에서 모든 request parameter 가 제공되지 않아도 정상 처리가 된다")
    void successGetNovelsWithNoRequestParams() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/novels")
        );

        // then
        action.andExpect(status().isOk());
        verify(novelQueryService).findNovels(any());
    }

    @Test
    @DisplayName("소설 신작 리스트 조회 에서 잘못된 sort 타입이 전달되면 검증 에러가 발생한다")
    void failedGetNewNovelsWithInvalidSortType() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/novels/new")
                        .param("sort", "invalid")
        );

        // then
        ResultMatcher[] commonResultMatchers = buildFailTestCommonResultMatchers(CommonErrorCode.VALIDATION_FAILED);
        action
                .andExpect(status().isBadRequest())
                .andExpectAll(commonResultMatchers);
    }

    @Test
    @DisplayName("소설 신작 리스트 조회 에서 모든 request parameter가 제공되지 않아도 정상 처리된다")
    void successGetNewNovelsWithNoParams() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/novels/new")
        );

        // then
        action.andExpect(status().isOk());
        verify(novelQueryService).findNewNovels(any());
    }

    @Test
    @DisplayName("소설 카테고리별 리스트 조회 에서 잘못된 sort 타입이 전달되면 검증 에러가 발생한다")
    void failedGetNovelsByCategoryWithInvalidSortType() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/novels/category/{category}", NovelCategory.ETC.getValue())
                        .param("sort", "invalid")
        );

        // then
        ResultMatcher[] commonResultMatchers = buildFailTestCommonResultMatchers(CommonErrorCode.VALIDATION_FAILED);
        action
                .andExpect(status().isBadRequest())
                .andExpectAll(commonResultMatchers);
    }

    @Test
    @DisplayName("소설 카테고리별 리스트 조회 에서 정상적인 값들이 전달되면 정상 처리 된다")
    void successGetNovelsByCategory() throws Exception {
        // when
        ResultActions action = mockMvc.perform(
                get("/novels/category/{category}", NovelCategory.ETC.getValue())
                        .param("sort", NovelSortType.LAST_UPDATE.getValue())
        );

        // then
        action.andExpect(status().isOk());
        verify(novelQueryService).findNovelsByCategory(any(), any());
    }

    @ParameterizedTest(name = "소설 생성 검증 실패: {0}")
    @MethodSource("createNovelInvalidCases")
    @DisplayName("소설 생성 에서 유효성 검증에 실패하면 검증 실패 예외가 발생한다")
    void failedCreateNovelValidation(String caseDesc, CreateNovelDto dto) throws Exception {
        // when
        ResultActions action = performCreateNovel(dto);

        // then
        ResultMatcher[] commonResultMatchers = buildFailTestCommonResultMatchers(CommonErrorCode.VALIDATION_FAILED);
        action
                .andExpect(status().isBadRequest())
                .andExpectAll(commonResultMatchers);
    }

    @Test
    @DisplayName("소설 생성 에서 중복된 제목이라면 예외가 발생한다")
    void failedCreateNovelWithDuplicateTitle() throws Exception {
        // given
        when(novelService.createNovel(any()))
                .thenThrow(new DuplicateNovelTitle("title"));

        // when
        ResultActions action = performCreateNovel(NovelDtoTestFactory.defaultCreateNovelDto());

        // then
        ResultMatcher[] commonResultMatchers = buildFailTestCommonResultMatchers(NovelErrorCode.DUPLICATE_TITLE);
        action
                .andExpect(status().is(NovelErrorCode.DUPLICATE_TITLE.getStatus().value()))
                .andExpectAll(
                        commonResultMatchers
                );
    }

    @Test
    @DisplayName("소설 생성 에서 정상적인 값들이 전달되면 정상 처리 된다")
    void successCreateNovel() throws Exception {
        // given
        CreateNovelDto dto = NovelDtoTestFactory.defaultCreateNovelDto();

        // when
        ResultActions action = performCreateNovel(dto);

        // then
        action.andExpect(status().isOk());
        verify(novelService).createNovel(any());
    }

    @ParameterizedTest(name = "소설 업데이트 검증 실패: {0}")
    @MethodSource("updateNovelInvalidCases")
    @DisplayName("소설 업데이트 에서 유효성 검증에 실패하면 검증 실패 예외가 발생한다")
    void failedUpdateNovelValidation(String caseDesc, UpdateNovelDto dto) throws Exception {
        // when
        ResultActions action = performUpdateNovel(dto, 1L);

        // then
        ResultMatcher[] commonResultMatchers = buildFailTestCommonResultMatchers(CommonErrorCode.VALIDATION_FAILED);
        action
                .andExpect(status().isBadRequest())
                .andExpectAll(commonResultMatchers);
    }

    @Test
    @DisplayName("소설 업데이트 에서 정상적인 값들이 전달되면 정상 처리 된다")
    void successUpdateNovel() throws Exception {
        // given
        UpdateNovelDto dto = NovelDtoTestFactory.defaultUpdateNovelDto();
        long novelId = 1L;

        // when
        ResultActions action = performUpdateNovel(dto, novelId);

        // then
        action.andExpect(status().isOk());
        verify(novelService).updateNovel(anyLong(), eq(novelId), any());
    }

    @Test
    @DisplayName("소설 좋아요 수 증가 에서 정상적인 값들이 전달되면 정상 처리 된다")
    void successAddLikeCount() throws Exception {
        // given
        long novelId = 1L;

        // when
        ResultActions action = mockMvc.perform(
                post("/novels/{novelId}/likes", novelId)
                        .with(csrf())
        );

        // then
        action.andExpect(status().isOk());
        verify(novelService).addLikeCount(anyLong(), eq(novelId));
    }

    @Test
    @DisplayName("소설 좋아요 수 감소 에서 정상적인 값들이 전달되면 정상 처리 된다")
    void successRemoveLikeCount() throws Exception {
        // given
        long novelId = 1L;

        // when
        ResultActions action = mockMvc.perform(
                delete("/novels/{novelId}/likes", novelId)
                        .with(csrf())
        );

        // then
        action.andExpect(status().isOk());
        verify(novelService).removeLikeCount(anyLong(), eq(novelId));
    }

    @Test
    @DisplayName("소설 삭제 에서 정상적인 값들이 전달되면 정상 처리 된다")
    void successDeleteNovel() throws Exception {
        // given
        long novelId = 1L;

        // when
        ResultActions action = mockMvc.perform(
                delete("/novels/{novelId}", novelId)
                        .with(csrf())
        );

        // then
        action.andExpect(status().isOk());
        verify(novelService).deleteNovel(anyLong(), eq(novelId));
    }

    private ResultActions performCreateNovel(CreateNovelDto dto) throws Exception {
        return mockMvc.perform(
                post("/novels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
        );
    }

    private ResultActions performUpdateNovel(UpdateNovelDto dto, long novelId) throws Exception {
        return mockMvc.perform(
                patch("/novels/{novelId}", novelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf())
        );
    }

    private ResultMatcher[] buildFailTestCommonResultMatchers(ErrorCode errorCode) {
        return new ResultMatcher[] {
                jsonPath("$.isSuccess").value(false),
                jsonPath("$.message").exists(),
                jsonPath("$.code").value(errorCode.getCode())
        };
    }
}
