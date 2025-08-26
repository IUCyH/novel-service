package com.iucyh.novelservice.domain.episode;

import com.iucyh.novelservice.domain.BaseDateEntity;
import com.iucyh.novelservice.domain.novel.Novel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.iucyh.novelservice.common.constant.EpisodeConstants.*;

@Entity
@Table(name = "episodes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Episode extends BaseDateEntity {

    @Column(nullable = false, length = EPISODE_TITLE_LENGTH_MAX)
    private String title;

    @Column(nullable = false, length = EPISODE_DESC_LENGTH_MAX)
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer episodeNumber;

    @Column(nullable = false)
    private Integer watchCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    public static Episode of(String title, String description, String content, Integer episodeNumber, Novel novel) {
        Episode episode = new Episode();
        episode.title = title;
        episode.description = description;
        episode.content = content;
        episode.episodeNumber = episodeNumber;
        episode.novel = novel;
        return episode;
    }
}
