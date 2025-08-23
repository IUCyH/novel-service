package com.iucyh.novelservice.repository.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.repository.IdResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class NovelRepositoryTest {

    @Autowired
    private NovelRepository novelRepository;

    @Test
    @DisplayName("Novel의 publicId로 id 조회 - 성공")
    void getIdByPublicIdWithSuccess() {
        // given
        Novel novel = Novel.of("test novel", "test object", NovelCategory.ETC);
        novelRepository.save(novel);

        // when
        Optional<IdResult> idResult = novelRepository.findIdByPublicId(novel.getPublicId());

        // then
        assertThat(idResult).isPresent();
        assertThat(idResult.get().getId()).isEqualTo(novel.getId());
    }

    @Test
    @DisplayName("Novel의 publicId로 id 조회 - 실패")
    void getIdByPublicIdWithFail() {
        // given
        Novel novel = Novel.of("test novel", "test object", NovelCategory.ETC);
        novelRepository.save(novel);

        // when
        Optional<IdResult> idResult = novelRepository.findIdByPublicId("unvalidpublicid");

        // then
        assertThat(idResult).isEmpty();
    }
}