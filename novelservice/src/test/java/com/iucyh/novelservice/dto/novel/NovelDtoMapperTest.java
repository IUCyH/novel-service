package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelDtoTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class NovelDtoMapperTest {
    
    @Test
    @DisplayName("Novel 엔티티 -> NovelDto 변환 시 필드가 올바르게 매핑된다")
    void toNovelDtoMappedCorrectly() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovelWithId();
        novel.updateLastEpisodeAt(LocalDateTime.now());
        
        // when
        NovelDto result = NovelDtoMapper.toNovelDto(novel);

        // then
        assertThat(result.getNovelId()).isEqualTo(novel.getId());
        assertThat(result.getTitle()).isEqualTo(novel.getTitle());
        assertThat(result.getDescription()).isEqualTo(novel.getDescription());
        assertThat(result.getCategory()).isEqualTo(novel.getCategory());
        assertThat(result.getLikeCount()).isEqualTo(novel.getLikeCount());
        assertThat(result.getTotalViewCount()).isEqualTo(novel.getTotalViewCount());
        assertThat(result.getLastUpdateDate()).isEqualTo(novel.getLastEpisodeAt());
        assertThat(result.getCreatedAt()).isEqualTo(novel.getCreatedAt());
    }

    @Test
    @DisplayName("NovelDto 리스트 -> PagingResultDto 변환 시 필드가 올바르게 매핑된다")
    void toPagingResultDtoMappedCorrectly() {
        // given
        NovelDto novelDto = NovelDtoTestFactory.defaultNovelDto();

        // when
        PagingResultDto<NovelDto> result = NovelDtoMapper.toPagingResultDto(List.of(novelDto), 10, "cursor");

        // then
        assertThat(result.getTotalCount()).isEqualTo(10);
        assertThat(result.getItems()).containsExactly(novelDto);
        assertThat(result.getNextCursor()).isEqualTo("cursor");
    }

    @Test
    @DisplayName("CreateNovelDto -> Novel 엔티티 변환 시 필드가 올바르게 매핑된다")
    void createNovelDtoToNovelMappedCorrectly() {
        // given
        CreateNovelDto createNovelDto = NovelDtoTestFactory.defaultCreateNovelDto();

        // when
        Novel result = NovelDtoMapper.toNovelEntity(createNovelDto);

        // then
        NovelCategory expectedCategory = NovelCategory.of(createNovelDto.getCategory());

        assertThat(result.getTitle()).isEqualTo(createNovelDto.getTitle());
        assertThat(result.getDescription()).isEqualTo(createNovelDto.getDescription());
        assertThat(result.getCategory()).isEqualTo(expectedCategory);
    }
}
