package com.iucyh.novelservice.episode.domain;

import com.iucyh.novelservice.common.domain.PublicEntity;
import com.iucyh.novelservice.novel.domain.Novel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.*;

@Entity
@Table(name = "episode")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Episode extends PublicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = EPISODE_TITLE_LENGTH_MAX, nullable = false)
    private String title;

    @Column(length = EPISODE_DESC_LENGTH_MAX, nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer episodeNumber;

    @Column(nullable = false)
    private Integer viewCount = 0;

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

    public void updateTextMetaData(String title, String description) {
        if (title != null) {
            this.title = title;
        }

        if (description != null) {
            this.description = description;
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
