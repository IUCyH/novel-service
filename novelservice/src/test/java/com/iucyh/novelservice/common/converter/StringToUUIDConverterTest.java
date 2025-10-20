package com.iucyh.novelservice.common.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class StringToUUIDConverterTest {

    private final DefaultConversionService conversionService = new DefaultConversionService();

    public StringToUUIDConverterTest() {
        conversionService.addConverter(new StringToUUIDConverter());
    }

    @Test
    @DisplayName("비어있는 값이 들어오면 변환이 실패한다")
    void convertFailWithEmptyValue() {
        // given
        String emptyValue = " ";

        // when
        UUID converted = conversionService.convert(emptyValue, UUID.class);

        // then
        assertThat(converted).isNull();
    }

    @Test
    @DisplayName("길이가 32가 아닌 문자열이 들어오면 변환이 실패한다")
    void convertFailWithInvalidLength() {
        // given
        String lessThanLength = "abc";
        String moreThanLength = UUID.randomUUID().toString(); // 하이픈 제거하지 않은 UUID

        // when
        UUID lessStrConverted = conversionService.convert(lessThanLength, UUID.class);
        UUID moreStrConverted = conversionService.convert(moreThanLength, UUID.class);

        // then
        assertThat(lessStrConverted).isNull();
        assertThat(moreStrConverted).isNull();
    }

    @Test
    @DisplayName("정확한 32자리의 UUID 문자열 값이 들어오면 성공한다")
    void convertSuccess() {
        // given
        String uuidStr = UUID.randomUUID().toString().replace("-", "");

        // when
        UUID converted = conversionService.convert(uuidStr, UUID.class);

        // then
        assertThat(converted).isNotNull();

        String convertedUUIDStr = converted.toString().replace("-", "");
        assertThat(convertedUUIDStr).isEqualTo(uuidStr);
    }
}
