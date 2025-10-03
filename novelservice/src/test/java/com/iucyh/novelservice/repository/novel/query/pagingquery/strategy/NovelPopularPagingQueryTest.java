package com.iucyh.novelservice.repository.novel.query.pagingquery.strategy;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.domain.novel.NovelPeriodView;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.NovelPopularQueryDto;
import com.iucyh.novelservice.repository.novel.NovelPeriodViewRepository;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelPopularCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelPopularPagingQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class NovelPopularPagingQueryTest {

    private static final int DUMMY_DATA_SIZE = 10;

    private final NovelRepository novelRepository;
    private final NovelPeriodViewRepository periodViewRepository;
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final NovelPopularPagingQuery pagingQuery;

    @Autowired
    public NovelPopularPagingQueryTest(NovelRepository novelRepository, NovelPeriodViewRepository periodViewRepository, EntityManager em) {
        this.novelRepository = novelRepository;
        this.periodViewRepository = periodViewRepository;
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.pagingQuery = new NovelPopularPagingQuery();
    }

    @BeforeEach
    void beforeEach() {
        createDummyData();
    }

    @Test
    @Order(1) // 다른 테스트에서도 캐스팅이 필요하므로 가장 먼저 테스트
    @DisplayName("페이징 쿼리 결과가 올바른 클래스 인스턴스를 반환")
    void queryResultIsCorrectInstance() {
        // given
        JPAQuery<? extends NovelPagingQueryDto> query = pagingQuery.createQuery(queryFactory, null);

        // when
        List<? extends NovelPagingQueryDto> result = query.fetch();

        // then
        assertThat(result)
                .isNotEmpty();
        assertThat(result.get(0))
                .isInstanceOf(NovelPopularQueryDto.class);
    }

    @Test
    @DisplayName("쿼리에서 Novel 엔티티를 정상적으로 inner join 함")
    void queryResultHasNovelEntity() {
        // given
        JPAQuery<? extends NovelPagingQueryDto> query = pagingQuery.createQuery(queryFactory, null);

        // when
        List<? extends NovelPagingQueryDto> result = query.fetch();

        // then
        assertThat(result)
                .isNotEmpty();
        assertThat(result.get(0).getNovel())
                .isNotNull();
    }

    @Test
    @DisplayName("페이징 쿼리가 정상적으로 정렬된 결과 반환 - 커서 미포함")
    void queryResultCorrectWithoutCursor() {
        // given
        JPAQuery<? extends NovelPagingQueryDto> query = pagingQuery.createQuery(queryFactory, null);

        // when
        List<? extends NovelPagingQueryDto> result = query.fetch();

        // then
        List<Integer> viewCounts = result.stream()
                .map(r -> {
                    NovelPopularQueryDto castedQueryDto = (NovelPopularQueryDto) r;
                    return castedQueryDto.getLastAggViewCount();
                })
                .toList();

        assertThat(result).hasSize(DUMMY_DATA_SIZE);
        // viewCount 기준 내림차순 정렬이 되었는지 검증
        assertThat(viewCounts)
                .isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("페이징 쿼리가 정상적으로 정렬된 결과 반환 - 커서 포함")
    void queryResultCorrectWithCursor() {
        // given
        // id가 가장 크면서도 viewCount 값도 가장 큰 데이터를 커서로 설정
        NovelPeriodView lastPeriodView = getLargestIdPeriodView();
        NovelPopularCursor cursor = new NovelPopularCursor(lastPeriodView.getId(), lastPeriodView.getViewCount());
        JPAQuery<? extends NovelPagingQueryDto> query = pagingQuery.createQuery(queryFactory, cursor);

        // when
        List<? extends NovelPagingQueryDto> result = query.fetch();

        // then

        // 커서로 설정한 데이터 보다 viewCount 가 더 작은 데이터를 전부 조회했는지 검증
        assertThat(result).hasSize(DUMMY_DATA_SIZE - 1); // size == 커서로 설정한 데이터 1개를 제외한 나머지

        // viewCount 가 동일할 때 id가 더 작은 데이터를 조회했는지 검증
        NovelPopularQueryDto firstElement = (NovelPopularQueryDto) result.get(0);

        assertThat(firstElement.getLastAggViewCount())
                .isEqualTo(lastPeriodView.getViewCount());
        assertThat(firstElement.getLastAggId())
                .isLessThan(lastPeriodView.getId());
    }

    @Test
    @DisplayName("커서 생성 메서드가 정상적인 커서 반환")
    void pagingQueryReturnsCorrectCursor() {
        // given
        NovelPeriodView lastPeriodView = getLargestIdPeriodView();
        NovelPopularQueryDto queryDto = new NovelPopularQueryDto(lastPeriodView.getNovel(), lastPeriodView.getId(), lastPeriodView.getViewCount());

        // when
        NovelCursor cursor = pagingQuery.createCursor(queryDto);

        // then
        assertThat(cursor)
                .isInstanceOf(NovelPopularCursor.class);

        NovelPopularCursor castedCursor = (NovelPopularCursor) cursor;

        assertThat(castedCursor.getLastAggId())
                .isEqualTo(lastPeriodView.getId());
        assertThat(castedCursor.getLastAggViewCount())
                .isEqualTo(lastPeriodView.getViewCount());
    }

    private void createDummyData() {
        LocalDate now = LocalDate.now().minusDays(3);
        for (int i = 0; i < DUMMY_DATA_SIZE; i++) {
            int sequence = i + 1;
            int viewCount = i;

            // 마지막 2개 데이터는 tie-breaker 테스트 위해 viewCount 가 동일하도록 강제
            if (sequence == DUMMY_DATA_SIZE || sequence == DUMMY_DATA_SIZE - 1) {
                viewCount = DUMMY_DATA_SIZE; // 임의로 같은 조회수를 갖도록 설정
            }

            Novel novel = Novel.of("novel_" + sequence, "", NovelCategory.ETC);
            NovelPeriodView periodView = NovelPeriodView.of(now, novel);
            periodView.addViews(viewCount);

            novelRepository.save(novel);
            periodViewRepository.save(periodView);
        }

        em.flush();
        em.clear();
    }

    private NovelPeriodView getLargestIdPeriodView() {
        return periodViewRepository.findAll()
                .stream()
                .max(Comparator.comparing(NovelPeriodView::getId))
                .orElseThrow(() -> new IllegalArgumentException("Period View Not Found"));
    }
}
