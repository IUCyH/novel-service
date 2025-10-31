package com.iucyh.novelservice.service.episode;

import com.iucyh.novelservice.common.exception.novel.NovelNotFound;
import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.dto.episode.mapper.EpisodeRequestMapper;
import com.iucyh.novelservice.dto.episode.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;
import com.iucyh.novelservice.repository.episode.EpisodeRepository;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EpisodeService {

    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    public EpisodeResponse createEpisode(long novelId, CreateEpisodeRequest request) {
        Novel novel = findNovel(novelId);
        Integer lastEpisodeNumber = episodeRepository.findLastEpisodeNumber(novelId)
                .orElse(0);

        int nextEpisodeNumber = lastEpisodeNumber + 1;
        Episode episode = EpisodeRequestMapper.toEpisodeEntity(request, novel, nextEpisodeNumber);

        Episode savedEpisode = episodeRepository.save(episode);
        novel.updateLastEpisodeAt(savedEpisode.getCreatedAt());

        return EpisodeResponseMapper.toEpisodeResponse(savedEpisode);
    }

    private Novel findNovel(long novelId) {
        return novelRepository.findById(novelId)
                .orElseThrow(() -> new NovelNotFound(novelId));
    }
}
