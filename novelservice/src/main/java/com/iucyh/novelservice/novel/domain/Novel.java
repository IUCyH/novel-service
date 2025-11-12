package com.iucyh.novelservice.novel.domain;

import com.iucyh.novelservice.common.domain.PublicEntity;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.iucyh.novelservice.novel.constant.NovelConstants.*;

@Entity
@Table(name = "novel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Novel extends PublicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = NOVEL_TITLE_LENGTH_MAX, nullable = false)
    private String title;

    @Column(length = NOVEL_DESC_LENGTH_MAX, nullable = false)
    private String description;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private NovelCategory category;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer totalViewCount = 0;

    @Column(nullable = false)
    private Integer prologueDeleteCount = 0;

    @Column(nullable = false)
    private Integer lastEpisodeNumber = 0;

    private LocalDateTime lastEpisodeAt;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Novel of(String title, String description, NovelCategory category, User user) {
        Novel novel = new Novel();
        novel.title = title;
        novel.description = description;
        novel.category = category;
        novel.user = user;
        return novel;
    }

    public void updateTextMetaData(String title, String description) {
        if (title != null) {
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

    public void updateLastEpisode(Integer lastEpisodeNumber, LocalDateTime lastEpisodeAt) {
        this.lastEpisodeNumber = lastEpisodeNumber;
        this.lastEpisodeAt = lastEpisodeAt;
    }
}
