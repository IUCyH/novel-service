package com.iucyh.novelservice.repository.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
}
