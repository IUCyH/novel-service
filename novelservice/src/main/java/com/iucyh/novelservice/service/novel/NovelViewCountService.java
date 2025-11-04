package com.iucyh.novelservice.service.novel;

import com.iucyh.novelservice.repository.episode.EpisodeRepository;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
public class NovelViewCountService {

    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    /**
     * 소설과 회차의 조회수를 각각 1씩 증가시키는 메서드
     */
    public void increaseViewCounts(long novelId, long episodeId) {
        novelRepository.increaseTotalViewCount(novelId);
        episodeRepository.increaseViewCount(episodeId);
    }
}
