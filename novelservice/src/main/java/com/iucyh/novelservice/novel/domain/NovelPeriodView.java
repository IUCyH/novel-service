package com.iucyh.novelservice.novel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "novel_period_views")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NovelPeriodView {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    public static NovelPeriodView of(LocalDate startDate, Novel novel) {
        NovelPeriodView periodView = new NovelPeriodView();
        periodView.startDate = startDate;
        periodView.novel = novel;
        return periodView;
    }
}
