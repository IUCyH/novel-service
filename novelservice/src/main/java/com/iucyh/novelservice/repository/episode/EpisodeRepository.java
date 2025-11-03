package com.iucyh.novelservice.repository.episode;

import com.iucyh.novelservice.domain.episode.Episode;
import com.iucyh.novelservice.repository.episode.projection.EpisodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    @Query("select max(e.episodeNumber) from Episode e where e.novel.id = :novelId")
    Optional<Integer> findLastEpisodeNumber(@Param("novelId") long novelId);

    @Query("select e from Episode e where e.id = :episodeId and e.novel.id = :novelId and e.deletedAt is null")
    Optional<Episode> findByIdAndNovelId(@Param("episodeId") long id, @Param("novelId") long novelId);

    @Query("select count(e) from Episode e where e.novel.id = :novelId and e.deletedAt is null")
    int countByNovelId(@Param("novelId") long novelId);

    @Query("select e.id as id, e.content as content from Episode e where e.novel.id = :novelId and e.episodeNumber = :episodeNumber and e.deletedAt is null")
    Optional<EpisodeDetail> findEpisodeDetail(@Param("novelId") long novelId, @Param("episodeNumber") int episodeNumber);
}
