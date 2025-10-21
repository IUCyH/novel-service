package com.iucyh.novelservice.service.novel;

import com.iucyh.novelservice.common.exception.novel.DuplicateNovelTitle;
import com.iucyh.novelservice.common.exception.novel.NovelNotFound;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.IdDto;
import com.iucyh.novelservice.dto.novel.CreateNovelDto;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
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
        CreateNovelDto dto = new CreateNovelDto("test", "test desc", NovelCategory.ETC.getValue());
        when(novelRepository.existsByTitle(Mockito.anyString()))
                .thenReturn(true);

        // then
        assertThatThrownBy(() -> novelService.createNovel(dto))
                .isInstanceOf(DuplicateNovelTitle.class);
    }

    @Test
    @DisplayName("소설 생성이 성공하면 생성된 소설의 public id 를 반환한다")
    void createNovelSuccess() {
        // given
        CreateNovelDto dto = new CreateNovelDto("test", "test desc", NovelCategory.ETC.getValue());

        // when
        IdDto result = novelService.createNovel(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPublicId()).isNotNull();
        assertThat(result.getPublicId()).hasSize(32);
    }

    @Test
    @DisplayName("소설의 좋아요 수가 정확히 1씩 증가한다")
    void likeCountOnlyIncreasesByOne() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);
        when(novelRepository.findByPublicId(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(novel));

        // when
        novelService.addLikeCount(1, novel.getPublicId());

        // then
        assertThat(novel.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("소설의 좋아요 수가 정확히 1씩 감소한다")
    void likeCountOnlyDecreasesByOne() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);
        novel.addLikes(1);

        when(novelRepository.findByPublicId(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(novel));

        // when
        novelService.removeLikeCount(1, novel.getPublicId());

        // then
        assertThat(novel.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("쓰기 메서드에서 소설 엔티티를 찾지 못하면 에러를 던진다")
    void cannotFindNovelInWriteMethod() {
        // given
        UUID uuid = UUID.randomUUID();
        when(novelRepository.findByPublicId(uuid))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> novelService.deleteNovel(1, uuid))
                .isInstanceOf(NovelNotFound.class);
    }
}
