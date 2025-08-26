package com.iucyh.novelservice.domain.novel;

import com.iucyh.novelservice.domain.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.iucyh.novelservice.common.constant.NovelConstants.*;

@Entity
@Table(name = "novels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Novel extends BaseDateEntity {

    @Column(nullable = false, length = NOVEL_TITLE_LENGTH_MAX)
    private String title;

    @Column(nullable = false, length = NOVEL_DESC_LENGTH_MAX)
    private String description;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NovelCategory category;

    public static Novel of(String title, String description, NovelCategory category) {
        Novel novel = new Novel();
        novel.title = title;
        novel.description = description;
        novel.category = category;
        return novel;
    }
}
