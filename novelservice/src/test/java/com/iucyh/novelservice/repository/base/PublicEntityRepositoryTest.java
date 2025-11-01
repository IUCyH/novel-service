package com.iucyh.novelservice.repository.base;

import com.fasterxml.uuid.Generators;
import com.iucyh.novelservice.repository.base.testsupport.TestPublicEntity;
import com.iucyh.novelservice.repository.base.testsupport.TestPublicRepository;
import com.iucyh.novelservice.testsupport.annotation.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@RepositoryTest
public class PublicEntityRepositoryTest {

    @Autowired
    private TestPublicRepository testRepository;

    @Test
    @DisplayName("test의 publicId로 entity 조회 - 성공")
    void findByPublicIdWithSuccess() {
        // given
        TestPublicEntity testEntity = new TestPublicEntity("test1");
        testRepository.save(testEntity);

        // when
        Optional<TestPublicEntity> result = testRepository.findByPublicId(testEntity.getPublicId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(testEntity.getName());
    }

    @Test
    @DisplayName("test의 publicId로 entity 조회 - 실패")
    void findByPublicIdWithFail() {
        // given
        TestPublicEntity testEntity = new TestPublicEntity("test1");
        testRepository.save(testEntity);

        // when
        UUID wrongPublicId = Generators.timeBasedEpochGenerator().generate();
        Optional<TestPublicEntity> result = testRepository.findByPublicId(wrongPublicId);

        // then
        assertThat(result).isEmpty();
    }
}
