package com.iucyh.novelservice.repository.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class PublicEntityRepositoryTest {

    @Autowired
    private TestRepository testRepository;

    @Test
    @DisplayName("test의 publicId로 entity 조회 - 성공")
    void findByPublicIdWithSuccess() {
        // given
        TestEntity testEntity = new TestEntity("test1");
        testRepository.save(testEntity);

        // when
        Optional<TestEntity> result = testRepository.findByPublicId(testEntity.getPublicId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(testEntity.getName());
    }

    @Test
    @DisplayName("test의 publicId로 entity 조회 - 실패")
    void findByPublicIdWithFail() {
        // given
        TestEntity testEntity = new TestEntity("test1");
        testRepository.save(testEntity);

        // when
        Optional<TestEntity> result = testRepository.findByPublicId("unknown");

        // then
        assertThat(result).isEmpty();
    }
}
