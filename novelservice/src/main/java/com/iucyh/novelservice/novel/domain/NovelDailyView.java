package com.iucyh.novelservice.novel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "novel_daily_views")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NovelDailyView {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDate viewDate;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    public static NovelDailyView of(LocalDate viewDate, Novel novel) {
        NovelDailyView dailyView = new NovelDailyView();
        dailyView.viewDate = viewDate;
        dailyView.novel = novel;
        return dailyView;
    }
}
