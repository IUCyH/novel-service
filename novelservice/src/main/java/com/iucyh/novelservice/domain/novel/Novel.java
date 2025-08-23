package com.iucyh.novelservice.domain.novel;

import com.iucyh.novelservice.domain.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "novels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Novel extends BaseDateEntity {

    @Column(nullable = false, length = 64)
    private String title;

    @Column(nullable = false, length = 32)
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
