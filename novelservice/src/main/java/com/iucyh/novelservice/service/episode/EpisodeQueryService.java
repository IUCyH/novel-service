package com.iucyh.novelservice.service.episode;

import com.iucyh.novelservice.common.exception.episode.EpisodeNotFound;
import com.iucyh.novelservice.novel.exception.NovelNotFound;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.episode.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.dto.episode.query.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.dto.episode.request.EpisodePagingRequest;
import com.iucyh.novelservice.dto.episode.response.EpisodeDetailResponse;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;
import com.iucyh.novelservice.repository.episode.EpisodeRepository;
import com.iucyh.novelservice.repository.episode.projection.EpisodeDetail;
import com.iucyh.novelservice.repository.episode.query.EpisodeQueryRepository;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.novel.service.NovelViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EpisodeQueryService {

    private final NovelViewCountService novelViewCountService;
    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;
    private final EpisodeQueryRepository episodeQueryRepository;

    public PagingResultDto<EpisodeResponse> findEpisodesByNovel(long novelId, EpisodePagingRequest request) {
        boolean novelExists = novelRepository.existsById(novelId);
        if (!novelExists) {
            throw new NovelNotFound(novelId);
        }

        List<EpisodeSimpleQueryDto> result = episodeQueryRepository.findEpisodesByNovelId(novelId, request.lastEpisode(), request.limit());
        int episodeCount = episodeRepository.countByNovelId(novelId);

        if (result.isEmpty()) {
            return EpisodeResponseMapper.toPagingResultDto(List.of(), episodeCount, null);
        }

        List<EpisodeResponse> episodeResponses = mapToEpisodeResponseList(result);
        int lastEpisodeNumber = result.get(result.size() - 1).getEpisodeNumber();
        return EpisodeResponseMapper.toPagingResultDto(episodeResponses, episodeCount, lastEpisodeNumber);
    }

    public EpisodeDetailResponse findEpisodeDetail(long novelId, int episodeNumber) {
        EpisodeDetail detail = episodeRepository.findEpisodeDetail(novelId, episodeNumber)
                .orElseThrow(() -> EpisodeNotFound.withEpisodeNumber(episodeNumber));

        try {
            novelViewCountService.increaseViewCounts(novelId, detail.getId());
        } catch (DataAccessException e) {
            log.warn("Failed to increase view count for episode {} of novel {}", detail.getId(), novelId, e);
        }

        return EpisodeResponseMapper.toEpisodeDetailResponse(detail);
    }

    private List<EpisodeResponse> mapToEpisodeResponseList(List<EpisodeSimpleQueryDto> episodes) {
        return episodes.stream()
                .map(EpisodeResponseMapper::toEpisodeResponse)
                .toList();
    }
}
