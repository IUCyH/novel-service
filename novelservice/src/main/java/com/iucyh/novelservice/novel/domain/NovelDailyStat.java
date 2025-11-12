package com.iucyh.novelservice.novel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "novel_daily_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NovelDailyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate statDate;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    public static NovelDailyStat of(LocalDate statDate) {
        NovelDailyStat stat = new NovelDailyStat();
        stat.statDate = statDate;
        return stat;
    }
}
