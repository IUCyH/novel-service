package com.iucyh.novelservice.common.enumtype.valuedenum;

import com.iucyh.novelservice.common.testsupport.TestValuedEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ValuedEnumHelperTest {

    @Test
    @DisplayName("value가 null 이면 결과는 null 이 된다")
    void failedConvertToValuedEnumWithNullValue() {
        // when
        TestValuedEnum result = TestValuedEnum.of(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("value가 빈 값이면 결과는 null 이 된다")
    void failedConvertToValuedEnumWithEmptyValue() {
        // when
        TestValuedEnum result = TestValuedEnum.of(" ");

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("value가 정확한 enum 값이면 정상적으로 결과가 나온다")
    void successConvertToValuedEnum() {
        // given
        String validValue = TestValuedEnum.A.getValue();

        // when
        TestValuedEnum result = TestValuedEnum.of(validValue);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(validValue);
    }
}
