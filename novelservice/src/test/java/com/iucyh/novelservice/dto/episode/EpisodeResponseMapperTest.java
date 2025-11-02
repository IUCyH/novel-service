package com.iucyh.novelservice.dto.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.episode.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.dto.episode.query.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.dto.episode.response.EpisodeDetailResponse;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeDtoTestFactory;
import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeEntityTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class EpisodeResponseMapperTest {

    @Test
    @DisplayName("Episode -> EpisodeResponse 변환 시 올바르게 매핑된다")
    void toEpisodeResponseMappedCorrectly() {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisodeWithId();

        // when
        EpisodeResponse result = EpisodeResponseMapper.toEpisodeResponse(episode);

        // then
        assertThat(result.episodeId()).isEqualTo(episode.getId());
        assertThat(result.title()).isEqualTo(episode.getTitle());
        assertThat(result.description()).isEqualTo(episode.getDescription());
        assertThat(result.episodeNumber()).isEqualTo(episode.getEpisodeNumber());
        assertThat(result.viewCount()).isEqualTo(episode.getViewCount());
        assertThat(result.updatedAt()).isEqualTo(episode.getUpdatedAt());
        assertThat(result.createdAt()).isEqualTo(episode.getCreatedAt());
    }

    @Test
    @DisplayName("EpisodeSimpleQueryDto -> EpisodeResponse 변환 시 올바르게 매핑된다")
    void SimpleQueryDtoToEpisodeResponseMappedCorrectly() {
        // given
        EpisodeSimpleQueryDto queryDto = EpisodeEntityTestFactory.defaultEpisodeSimpleQueryDto();

        // when
        EpisodeResponse result = EpisodeResponseMapper.toEpisodeResponse(queryDto);

        // then
        assertThat(result.episodeId()).isEqualTo(queryDto.getId());
        assertThat(result.title()).isEqualTo(queryDto.getTitle());
        assertThat(result.description()).isEqualTo(queryDto.getDescription());
        assertThat(result.episodeNumber()).isEqualTo(queryDto.getEpisodeNumber());
        assertThat(result.viewCount()).isEqualTo(queryDto.getViewCount());
        assertThat(result.updatedAt()).isEqualTo(queryDto.getUpdatedAt());
        assertThat(result.createdAt()).isEqualTo(queryDto.getCreatedAt());
    }

    @Test
    @DisplayName("Episode -> EpisodeDetailResponse 변환 시 올바르게 매핑된다")
    void toEpisodeDetailResponseMappedCorrectly() {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisode();

        // when
        EpisodeDetailResponse result = EpisodeResponseMapper.toEpisodeDetailResponse(episode);

        // then
        assertThat(result.content()).isEqualTo(episode.getContent());
    }

    @Test
    @DisplayName("EpisodeResponse 리스트 -> PagingResultDto 변환 시 올바르게 매핑된다")
    void toPagingResultDtoMappedCorrectly() {
        // given
        EpisodeResponse episodeResponse = EpisodeDtoTestFactory.defaultEpisodeResponse();

        // when
        PagingResultDto<EpisodeResponse> result = EpisodeResponseMapper.toPagingResultDto(List.of(episodeResponse), 10, 5);

        // then
        assertThat(result.getTotalCount()).isEqualTo(10);
        assertThat(result.getNextCursor()).isEqualTo(5);
        assertThat(result.getItems()).containsExactly(episodeResponse);
    }
}
