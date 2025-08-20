package com.iucyh.novelservice.domain;

import com.iucyh.novelservice.domain.novel.Novel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "episodes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Episode extends BaseDateEntity {

    @Id
    @GeneratedValue
    private Integer episodeId;

    @Column(nullable = false, updatable = false, length = 32)
    private String episodePublicId = generatePublicId();

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false, length = 20)
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
