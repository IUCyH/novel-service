package com.iucyh.novelservice.episode.service;

import com.iucyh.novelservice.episode.exception.EpisodeNotFound;
import com.iucyh.novelservice.episode.repository.query.condition.EpisodeSearchCondition;
import com.iucyh.novelservice.novel.exception.NovelNotFound;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.episode.web.dto.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.episode.repository.query.dto.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.episode.web.dto.request.EpisodePagingRequest;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeDetailResponse;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeResponse;
import com.iucyh.novelservice.episode.repository.EpisodeRepository;
import com.iucyh.novelservice.episode.repository.projection.EpisodeDetail;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
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

        EpisodeSearchCondition searchCondition = new EpisodeSearchCondition(request.lastEpisode(), request.limit());
        List<EpisodeSimpleQueryDto> result = episodeQueryRepository.findEpisodesByNovelId(novelId, searchCondition);
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
