package com.iucyh.novelservice.novel.repository;

import com.iucyh.novelservice.novel.domain.NovelPeriodStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NovelPeriodViewRepository extends JpaRepository<NovelPeriodStat, Long> {
}
