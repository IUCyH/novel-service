package com.iucyh.novelservice.repository.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.repository.PublicEntityRepository;

public interface NovelRepository extends PublicEntityRepository<Novel, Long> {

    boolean existsByTitle(String title);
}
