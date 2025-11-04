package com.iucyh.novelservice.service.episode;

import com.iucyh.novelservice.common.exception.episode.EpisodeNotFound;
import com.iucyh.novelservice.novel.exception.NovelNotFound;
import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.dto.episode.mapper.EpisodeRequestMapper;
import com.iucyh.novelservice.dto.episode.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeDetailRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.response.EpisodeDetailResponse;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;
import com.iucyh.novelservice.repository.episode.EpisodeRepository;
import com.iucyh.novelservice.novel.repository.NovelRepository;
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

    public EpisodeResponse updateEpisode(long novelId, long episodeId, UpdateEpisodeRequest request) {
        Episode episode = findEpisodeWithNovelId(novelId, episodeId);
        episode.updateTextMetaData(request.title(), request.description());

        return EpisodeResponseMapper.toEpisodeResponse(episode);
    }

    public EpisodeDetailResponse updateEpisodeDetail(long novelId, long episodeId, UpdateEpisodeDetailRequest request) {
        Episode episode = findEpisodeWithNovelId(novelId, episodeId);
        episode.updateContent(request.content());

        return EpisodeResponseMapper.toEpisodeDetailResponse(episode);
    }

    public void deleteEpisode(long novelId, long episodeId) {
        Episode episode = findEpisodeWithNovelId(novelId, episodeId);
        episode.softDelete();
    }

    private Novel findNovel(long novelId) {
        return novelRepository.findById(novelId)
                .orElseThrow(() -> new NovelNotFound(novelId));
    }

    private Episode findEpisodeWithNovelId(long novelId, long episodeId) {
        return episodeRepository.findByIdAndNovelId(episodeId, novelId)
                .orElseThrow(() -> new EpisodeNotFound(episodeId));
    }
}
