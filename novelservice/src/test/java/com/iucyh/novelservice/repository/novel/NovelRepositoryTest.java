package com.iucyh.novelservice.repository.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.testsupport.annotation.RepositoryTest;
import com.iucyh.novelservice.testsupport.testfactory.novel.NovelEntityTestFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
public class NovelRepositoryTest {

    private final NovelRepository novelRepository;
    private final EntityManager em;

    @Autowired
    public NovelRepositoryTest(NovelRepository novelRepository, EntityManager em) {
        this.novelRepository = novelRepository;
        this.em = em;
    }

    @Test
    @DisplayName("소설 제목 존재 여부 쿼리가 정상적인 결과를 반환한다")
    void existsByTitleSuccess() {
        // given
        Novel novel = Novel.of("exists", "desc", NovelCategory.ETC);
        saveDummyData(novel);

        // when
        boolean exists = novelRepository.existsByTitle(novel.getTitle());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("삭제된 소설을 제외한 나머지 개수가 정확하게 카운팅 된다")
    void countWithoutDeletedNovelsSuccess() {
        // given
        Novel novel = Novel.of("not deleted", "desc", NovelCategory.ETC);
        Novel deletedNovel = Novel.of("deleted", "desc", NovelCategory.ETC);

        deletedNovel.softDelete();

        saveDummyData(novel);
        saveDummyData(deletedNovel);

        // when
        long result = novelRepository.countByDeletedAtIsNull();

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("소설의 총 조회수 증가 업데이트에서 소설이 삭제되었다면 실패한다")
    void failedIncreaseTotalViewCountWhenNovelIsDeleted() {
        // given
        Novel novel = NovelEntityTestFactory.defaultNovel();

        novel.softDelete();
        saveDummyData(novel);

        // when
        novelRepository.increaseTotalViewCount(novel.getId());

        // then
        Optional<Novel> foundNovel = novelRepository.findById(novel.getId());
        int expectedViewCount = novel.getTotalViewCount();

        assertThat(foundNovel).isPresent();
        assertThat(foundNovel.get().getTotalViewCount())
                .isEqualTo(expectedViewCount);
    }

    @Test
    @DisplayName("소설의 총 조회수 증가 업데이트가 성공한다")
    void successIncreaseTotalViewCount() {
        // given
        Novel novel1 = NovelEntityTestFactory.defaultNovel();
        Novel novel2 = NovelEntityTestFactory.defaultNovel();

        saveDummyData(novel1);
        saveDummyData(novel2);

        // when
        novelRepository.increaseTotalViewCount(novel1.getId());

        // then
        Optional<Novel> foundNovel1 = novelRepository.findById(novel1.getId());
        Optional<Novel> foundNovel2 = novelRepository.findById(novel2.getId());

        int novel1ExpectedViewCount = novel1.getTotalViewCount() + 1;
        int novel2ExpectedViewCount = novel2.getTotalViewCount();

        assertThat(foundNovel1).isPresent();
        assertThat(foundNovel1.get().getTotalViewCount())
                .isEqualTo(novel1ExpectedViewCount);

        assertThat(foundNovel2).isPresent();
        assertThat(foundNovel2.get().getTotalViewCount())
                .isEqualTo(novel2ExpectedViewCount);
    }

    private void saveDummyData(Novel novel) {
        novelRepository.save(novel);
        em.flush();
        em.clear();
    }
}
