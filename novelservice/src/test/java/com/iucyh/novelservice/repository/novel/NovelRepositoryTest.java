package com.iucyh.novelservice.repository.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.testsupport.annotation.RepositoryTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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

    private void saveDummyData(Novel novel) {
        novelRepository.save(novel);
        em.flush();
        em.clear();
    }
}
