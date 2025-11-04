package com.iucyh.novelservice.episode.repository.query;

import com.iucyh.novelservice.episode.repository.query.dto.EpisodeSimpleQueryDto;
import com.iucyh.novelservice.episode.repository.query.dto.QEpisodeSimpleQueryDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iucyh.novelservice.episode.domain.QEpisode.episode;

@Repository
@RequiredArgsConstructor
public class EpisodeQueryRepositoryImpl implements EpisodeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EpisodeSimpleQueryDto> findEpisodesByNovelId(long novelId, Integer lastEpisodeNumber, int limit) {
        JPAQuery<EpisodeSimpleQueryDto> query = queryFactory
                .select(new QEpisodeSimpleQueryDto(
                        episode.id,
                        episode.title,
                        episode.description,
                        episode.episodeNumber,
                        episode.viewCount,
                        episode.updatedAt,
                        episode.createdAt
                ))
                .from(episode)
                .where(
                        episode.novel.id.eq(novelId),
                        episode.deletedAt.isNull()
                )
                .orderBy(episode.episodeNumber.desc())
                .limit(limit);
        if (isNotFirstPage(lastEpisodeNumber)) {
            query.where(episode.episodeNumber.lt(lastEpisodeNumber));
        }

        return query.fetch();
    }

    private boolean isNotFirstPage(Integer lastEpisodeNumber) {
        return lastEpisodeNumber != null;
    }
}
