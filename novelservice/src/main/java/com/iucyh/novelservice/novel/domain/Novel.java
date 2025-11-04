package com.iucyh.novelservice.novel.domain;

import com.iucyh.novelservice.domain.DateEntity;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.iucyh.novelservice.novel.constant.NovelConstants.*;

@Entity
@Table(name = "novels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Novel extends DateEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = NOVEL_TITLE_LENGTH_MAX)
    private String title;

    @Column(nullable = false, length = NOVEL_DESC_LENGTH_MAX)
    private String description;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer totalViewCount = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NovelCategory category;

    @Column
    private LocalDateTime lastEpisodeAt;

    public static Novel of(String title, String description, NovelCategory category) {
        Novel novel = new Novel();
        novel.title = title;
        novel.description = description;
        novel.category = category;
        return novel;
    }

    public void updateTextMetaData(String title, String description) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }

        if (description != null) {
            this.description = description;
        }
    }

    public void updateCategory(NovelCategory category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void addLikes(int count) {
        if (count > 0) {
            this.likeCount += count;
        }
    }

    public void removeLikes(int count) {
        if (count > 0) {
            this.likeCount = Math.max(0, this.likeCount - count);
        }
    }

    public void addViews(int count) {
        if (count > 0) {
            this.totalViewCount += count;
        }
    }

    public void updateLastEpisodeAt(LocalDateTime lastEpisodeAt) {
        this.lastEpisodeAt = lastEpisodeAt == null ? LocalDateTime.now() : lastEpisodeAt;
    }
}
