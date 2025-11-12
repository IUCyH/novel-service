package com.iucyh.novelservice.novel.domain;

import com.iucyh.novelservice.novel.enumtype.NovelStatPeriodType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "novel_period_stats")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NovelPeriodStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private NovelStatPeriodType periodType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    public static NovelPeriodStat of(NovelStatPeriodType periodType, LocalDate startDate, LocalDate endDate) {
        NovelPeriodStat stat = new NovelPeriodStat();
        stat.periodType = periodType;
        stat.startDate = startDate;
        stat.endDate = endDate;
        return stat;
    }
}
