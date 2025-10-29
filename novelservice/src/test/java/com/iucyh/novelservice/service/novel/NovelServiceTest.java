package com.iucyh.novelservice.service.novel;

import com.iucyh.novelservice.common.exception.novel.DuplicateNovelTitle;
import com.iucyh.novelservice.common.exception.novel.NovelErrorCode;
import com.iucyh.novelservice.common.exception.novel.NovelNotFound;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.*;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelDtoTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NovelServiceTest {

    @Mock
    private NovelRepository novelRepository;

    @InjectMocks
    private NovelService novelService;

    @Test
    @DisplayName("제목이 중복되었을 때 소설 생성이 실패하고 에러를 던진다")
    void createNovelFailWithDuplicateTitle() {
        // given
        CreateNovelDto dto = NovelDtoTestFactory.defaultCreateNovelDto();
        when(novelRepository.existsByTitle(dto.getTitle()))
                .thenReturn(true);

        // when
        Throwable throwable = catchThrowable(() -> novelService.createNovel(dto));

        // then
        assertThat(throwable)
                .isInstanceOf(DuplicateNovelTitle.class);

        DuplicateNovelTitle duplicateTitle = (DuplicateNovelTitle) throwable;
        assertThat(duplicateTitle.getErrorCode())
                .isEqualTo(NovelErrorCode.DUPLICATE_TITLE);
    }

    @Test
    @DisplayName("소설 생성이 성공하면 생성된 소설을 반환한다")
    void createNovelSuccess() {
        // given
        CreateNovelDto dto = NovelDtoTestFactory.defaultCreateNovelDto();

        when(novelRepository.save(any()))
                .thenAnswer((Answer<Novel>) invocation -> {
                    Object[] args = invocation.getArguments();
                    Novel novel = (Novel) args[0];
                    ReflectionTestUtils.setField(novel, "id", 1L);

                    return novel;
                });

        // when
        NovelDto result = novelService.createNovel(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(dto.getTitle());
    }

    @Test
    @DisplayName("소설 업데이트가 성공하면 업데이트된 소설을 반환한다")
    void updateNovelSuccess() {
        // given
        Novel novel = NovelEntityTestFactory.oldNovelWithId();
        UpdateNovelDto updateNovelDto = NovelDtoTestFactory.defaultUpdateNovelDto();

        when(novelRepository.findById(novel.getId()))
                .thenReturn(Optional.of(novel));

        // when
        NovelDto updatedNovel = novelService.updateNovel(1, novel.getId(), updateNovelDto);

        // then
        assertThat(updatedNovel).isNotNull();
        assertThat(updatedNovel.getTitle()).isEqualTo(updateNovelDto.getTitle());
        assertThat(updatedNovel.getDescription()).isEqualTo(updateNovelDto.getDescription());
    }

    @Test
    @DisplayName("소설의 좋아요 수가 정확히 1씩 증가한다")
    void likeCountOnlyIncreasesByOne() {
        // given
        Novel novel = NovelEntityTestFactory.oldNovelWithId();

        when(novelRepository.findById(novel.getId()))
                .thenReturn(Optional.of(novel));

        // when
        UpdateLikeCountDto likeCountDto = novelService.addLikeCount(1, novel.getId());

        // then
        assertThat(novel.getLikeCount()).isEqualTo(1);
        assertThat(likeCountDto.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("소설의 좋아요 수가 정확히 1씩 감소한다")
    void likeCountOnlyDecreasesByOne() {
        // given
        Novel novel = NovelEntityTestFactory.oldNovelWithId();

        novel.addLikes(1);
        when(novelRepository.findById(novel.getId()))
                .thenReturn(Optional.of(novel));

        // when
        UpdateLikeCountDto likeCountDto = novelService.removeLikeCount(1, novel.getId());

        // then
        assertThat(novel.getLikeCount()).isEqualTo(0);
        assertThat(likeCountDto.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("쓰기 메서드에서 소설 엔티티를 찾지 못하면 에러를 던진다")
    void cannotFindNovelInWriteMethod() {
        // given
        long novelId = 1L;
        when(novelRepository.findById(novelId))
                .thenReturn(Optional.empty());

        // when
        Throwable throwable = catchThrowable(() -> novelService.deleteNovel(1, novelId));

        // then
        assertThat(throwable)
                .isInstanceOf(NovelNotFound.class);

        NovelNotFound novelNotFound = (NovelNotFound) throwable;
        assertThat(novelNotFound.getErrorCode())
                .isEqualTo(NovelErrorCode.NOVEL_NOT_FOUND);
    }
}
