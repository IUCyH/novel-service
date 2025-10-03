package com.iucyh.novelservice.repository.novel.query.pagingquery.strategy;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.NovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelLastUpdateCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelLastUpdatePagingQuery;
import com.iucyh.novelservice.repository.novel.query.pagingquery.helper.NovelPagingQueryTestHelper;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class NovelLastUpdatePagingQueryTest {

    private static final int DUMMY_DATA_SIZE = 10;

    private final NovelRepository novelRepository;
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final NovelLastUpdatePagingQuery pagingQuery;

    @Autowired
    public NovelLastUpdatePagingQueryTest(NovelRepository novelRepository, EntityManager em) {
        this.novelRepository = novelRepository;
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.pagingQuery = new NovelLastUpdatePagingQuery();
    }

    @BeforeEach
    void beforeEach() {
        createDummyData();
    }

    @Test
    @DisplayName("페이징 쿼리가 정상적으로 정렬된 결과 반환 - 커서 미포함")
    void queryResultCorrectWithoutCursor() {
        // given
        JPAQuery<? extends NovelPagingQueryDto> query = pagingQuery.createQuery(queryFactory, null);

        // when
        List<? extends NovelPagingQueryDto> result = query.fetch();

        // then
        List<LocalDateTime> lastEpisodeDateTimes = result.stream()
                .map(r -> r.getNovel().getLastEpisodeAt())
                .toList();

        assertThat(result).hasSize(DUMMY_DATA_SIZE);
        // lastEpisodeAt 기준 내림차순 정렬이 되었는지 검증
        assertThat(lastEpisodeDateTimes)
                .isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("페이징 쿼리가 정상적으로 정렬된 결과 반환 - 커서 포함")
    void queryResultCorrectWithCursor() {
        // given
        // id가 가장 크면서도 lastEpisodeAt 값도 가장 큰 데이터를 커서로 설정
        Novel lastNovel = NovelPagingQueryTestHelper.getLargestIdNovel(novelRepository);
        NovelLastUpdateCursor cursor = new NovelLastUpdateCursor(lastNovel.getId(), lastNovel.getLastEpisodeAt());
        JPAQuery<? extends NovelPagingQueryDto> query = pagingQuery.createQuery(queryFactory, cursor);

        // when
        List<? extends NovelPagingQueryDto> result = query.fetch();

        // then

        // 커서로 설정한 데이터 보다 lastEpisodeAt 이 더 작은 데이터를 전부 조회했는지 검증
        assertThat(result).hasSize(DUMMY_DATA_SIZE - 1); // size == 커서로 설정한 데이터 1개를 제외한 나머지

        // lastEpisodeAt 이 동일할 때 id가 더 작은 데이터를 조회했는지 검증
        Novel firstNovel = result.get(0).getNovel();

        assertThat(firstNovel.getLastEpisodeAt())
                .isEqualTo(lastNovel.getLastEpisodeAt());
        assertThat(firstNovel.getId())
                .isLessThan(lastNovel.getId());
    }

    @Test
    @DisplayName("커서 생성 메서드가 정상적인 커서 반환")
    void pagingQueryReturnsCorrectCursor() {
        // given
        Novel lastNovel = NovelPagingQueryTestHelper.getLargestIdNovel(novelRepository);
        NovelSimpleQueryDto queryDto = new NovelSimpleQueryDto(lastNovel);

        // when
        NovelCursor cursor = pagingQuery.createCursor(queryDto);

        // then
        assertThat(cursor)
                .isInstanceOf(NovelLastUpdateCursor.class);

        NovelLastUpdateCursor castedCursor = (NovelLastUpdateCursor) cursor;

        assertThat(castedCursor.getLastNovelId())
                .isEqualTo(lastNovel.getId());
        assertThat(castedCursor.getLastUpdateDate())
                .isEqualTo(lastNovel.getLastEpisodeAt());
    }

    private void createDummyData() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < DUMMY_DATA_SIZE; i++) {
            int sequence = i + 1;
            int secondsOffset = i;

            // 마지막 2개 데이터는 tie-breaker 테스트 위해 lastEpisodeAt 이 동일하도록 강제
            if (sequence == DUMMY_DATA_SIZE || sequence == DUMMY_DATA_SIZE - 1) {
                secondsOffset = DUMMY_DATA_SIZE; // 임의로 같은 offset을 갖도록 설정
            }

            Novel novel = Novel.of("novel_" + sequence, "", NovelCategory.ETC);
            novel.updateLastEpisodeAt(now.plusSeconds(secondsOffset));

            novelRepository.save(novel);
        }

        em.flush();
        em.clear();
    }
}
