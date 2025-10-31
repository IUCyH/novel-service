package com.iucyh.novelservice.repository.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    @Query("select max(e.episodeNumber) from Episode e where e.novel.id = :novelId")
    Optional<Integer> findLastEpisodeNumber(@Param("novelId") long novelId);
}
