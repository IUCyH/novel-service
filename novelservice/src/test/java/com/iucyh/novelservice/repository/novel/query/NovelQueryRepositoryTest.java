package com.iucyh.novelservice.repository.novel.query;

import com.iucyh.novelservice.common.annotation.RepositoryTest;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.repository.novel.query.testsupport.TestNovelPagingQuery;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RepositoryTest
public class NovelQueryRepositoryTest {

    private int totalDataSize = 0;
    private final TestNovelPagingQuery testPagingQuery = new TestNovelPagingQuery();
    private final NovelQueryRepository queryRepository;
    private final NovelRepository novelRepository;
    private final EntityManager em;

    @Autowired
    public NovelQueryRepositoryTest(
            NovelQueryRepository queryRepository,
            NovelRepository novelRepository,
            EntityManager em
    ) {
        this.queryRepository = queryRepository;
        this.novelRepository = novelRepository;
        this.em = em;
    }

    @BeforeEach
    void beforeEach() {
        // 총 데이터 개수 : 12개
        // 카테고리 분포 : LIFE 5개, SF 5개
        // 생성일 분포 : 이번달 생성 5개, 저번달 생성 5개
        // 기타 : soft delete 처리된 소설 1개, 에피소드가 없는 소설 1개
        totalDataSize = createTestNovels();
    }

    @Test
    @DisplayName("소설 리스트 조회")
    void repositoryReturnsList() {
        // given
        NovelSearchCondition condition = new NovelSearchCondition(null);

        // when
        List<? extends NovelPagingQueryDto> novels = queryRepository.findNovels(condition, testPagingQuery, totalDataSize);

        // then
        // 삭제된 소설 1개, 에피소드가 없는 소설 1개 총 2개 제외
        int expectedDataSize = totalDataSize - 2;

        testCommonCondition(novels);
        assertThat(novels).hasSize(expectedDataSize);
    }

    @Test
    @DisplayName("카테고리별 소설 리스트 조회")
    void repositoryReturnsListByCategory() {
        // given
        NovelSearchCondition condition = new NovelSearchCondition(null);
        NovelCategory targetCategory = NovelCategory.LIFE;

        // when
        List<? extends NovelPagingQueryDto> novels = queryRepository.findNovelsByCategory(condition, testPagingQuery, targetCategory, totalDataSize);

        // then
        testCommonCondition(novels);
        assertThat(novels).allMatch(n -> n.getNovel().getCategory() == targetCategory);
    }

    @Test
    @DisplayName("이번달에 생성된 신작 소설 리스트 조회")
    void repositoryReturnsListCreatedThisMonth() {
        // given
        NovelSearchCondition condition = new NovelSearchCondition(null);

        // when
        List<? extends NovelPagingQueryDto> novels = queryRepository.findNewNovels(condition, testPagingQuery, totalDataSize);

        // then
        testCommonCondition(novels);

        Month expectedMonth = LocalDateTime.now().getMonth();
        assertThat(novels).allMatch((n) -> {
            LocalDateTime createdAt = n.getNovel().getCreatedAt();
            return createdAt.getMonth() == expectedMonth;
        });
    }

    private void testCommonCondition(List<? extends NovelPagingQueryDto> novels) {
        assertThat(novels).allMatch(n -> n.getNovel().getDeletedAt() == null);
        assertThat(novels).allMatch(n -> n.getNovel().getLastEpisodeAt() != null);
    }

    private int createTestNovels() {
        List<Novel> testNovels = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int sequence = i + 1;

            Novel novel = createTestNovel(sequence);
            novel.updateLastEpisodeAt(LocalDateTime.now());
            if (sequence % 2 == 0) {
                novel.updateCategory(NovelCategory.SF);

                LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
                setCreatedAt(novel, lastMonth);
            }

            testNovels.add(novel);
        }

        Novel deletedNovel = Novel.of("novel-deleted", "", NovelCategory.ETC);
        Novel novelWithNoEpisodes = Novel.of("novel-no-episodes", "", NovelCategory.ETC);

        deletedNovel.softDelete();
        deletedNovel.updateLastEpisodeAt(LocalDateTime.now());

        testNovels.add(deletedNovel);
        testNovels.add(novelWithNoEpisodes);

        novelRepository.saveAll(testNovels);
        em.flush();
        em.clear();

        return testNovels.size();
    }

    private Novel createTestNovel(int sequence) {
        return Novel.of("novel-" + sequence, "", NovelCategory.LIFE);
    }

    private void setCreatedAt(Novel novel, LocalDateTime dateTime) {
        ReflectionTestUtils.setField(
                novel,
                "createdAt",
                dateTime
        );
    }
}
