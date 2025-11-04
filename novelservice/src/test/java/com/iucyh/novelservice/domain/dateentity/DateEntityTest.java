package com.iucyh.novelservice.domain.dateentity;

import com.iucyh.novelservice.domain.dateentity.testsupport.TestDateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DateEntityTest {

    @Test
    @DisplayName("softDelete 호출 시 deletedAt 필드가 현재 시간으로 설정된다")
    void successSoftDelete() {
        // given
        TestDateEntity entity = new TestDateEntity();

        // when
        entity.softDelete();

        // then
        assertThat(entity.getDeletedAt()).isNotNull();
    }
}
