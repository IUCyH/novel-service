package com.iucyh.novelservice.repository.novel.query.pagingquery.strategy;

import com.iucyh.novelservice.common.annotation.RepositoryTest;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.dto.novel.query.NovelSimpleQueryDto;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelLikeCountCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelLikeCountPagingQuery;
import com.iucyh.novelservice.repository.novel.query.testsupport.NovelPagingQueryTestHelper;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RepositoryTest
public class NovelLikeCountPagingQueryTest {

    private static final int DUMMY_DATA_SIZE = 10;

    private final NovelRepository novelRepository;
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final NovelLikeCountPagingQuery pagingQuery;

    @Autowired
    public NovelLikeCountPagingQueryTest(NovelRepository novelRepository, EntityManager em) {
        this.novelRepository = novelRepository;
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.pagingQuery = new NovelLikeCountPagingQuery();
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
        List<Integer> likeCounts = result.stream()
                .map(r -> r.getNovel().getLikeCount())
                .toList();

        assertThat(result).hasSize(DUMMY_DATA_SIZE);
        // likeCount 기준 내림차순 정렬이 되었는지 검증
        assertThat(likeCounts)
                .isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @DisplayName("페이징 쿼리가 정상적으로 정렬된 결과 반환 - 커서 포함")
    void queryResultCorrectWithCursor() {
        // given
        // id가 가장 크면서도 likeCount 값도 가장 큰 데이터를 커서로 설정
        Novel lastNovel = NovelPagingQueryTestHelper.getLargestIdNovel(novelRepository);
        NovelCursor cursor = new NovelLikeCountCursor(lastNovel.getId(), lastNovel.getLikeCount());
        JPAQuery<? extends NovelPagingQueryDto> query = pagingQuery.createQuery(queryFactory, cursor);

        // when
        List<? extends NovelPagingQueryDto> result = query.fetch();

        // then

        // 커서로 설정한 데이터 보다 likeCount 가 더 작은 데이터를 전부 조회했는지 검증
        assertThat(result)
                .hasSize(DUMMY_DATA_SIZE - 1); // size == 커서로 설정한 데이터 1개를 제외한 나머지

        // likeCount 가 동일할 때 id가 더 작은 데이터를 조회했는지 검증
        Novel firstNovel = result.get(0).getNovel();

        assertThat(firstNovel.getLikeCount())
                .isEqualTo(lastNovel.getLikeCount());
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
                .isInstanceOf(NovelLikeCountCursor.class);

        NovelLikeCountCursor castedCursor = (NovelLikeCountCursor) cursor;

        assertThat(castedCursor.getLastNovelId())
                .isEqualTo(lastNovel.getId());
        assertThat(castedCursor.getLastLikeCount())
                .isEqualTo(lastNovel.getLikeCount());
    }

    private void createDummyData() {
        for (int i = 0; i < DUMMY_DATA_SIZE; i++) {
            int sequence = i + 1;
            int likeCount = i;

            // 마지막 2개 데이터는 tie-breaker 테스트 위해 likeCount 가 동일하도록 강제
            if (sequence == DUMMY_DATA_SIZE || sequence == DUMMY_DATA_SIZE - 1) {
                likeCount = DUMMY_DATA_SIZE; // 임의로 같은 좋아요 수를 갖도록 설정
            }

            Novel novel = Novel.of("novel_" + sequence, "", NovelCategory.ETC);
            novel.addLikes(likeCount);

            novelRepository.save(novel);
        }

        em.flush();
        em.clear();
    }
}
