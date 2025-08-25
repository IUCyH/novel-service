package com.iucyh.novelservice.repository.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.repository.BaseRepository;

public interface NovelRepository extends BaseRepository<Novel, Long> {

    boolean existsByTitle(String title);
}
