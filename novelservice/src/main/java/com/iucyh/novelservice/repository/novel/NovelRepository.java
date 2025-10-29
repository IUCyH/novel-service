package com.iucyh.novelservice.repository.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NovelRepository extends JpaRepository<Novel, Long> {

    boolean existsByTitle(String title);
    long countByDeletedAtIsNull();
}
