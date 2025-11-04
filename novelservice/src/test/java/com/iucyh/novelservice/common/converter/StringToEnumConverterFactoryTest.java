package com.iucyh.novelservice.common.converter;

import com.iucyh.novelservice.common.testsupport.TestValuedEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.assertThat;

public class StringToEnumConverterFactoryTest {

    private final DefaultConversionService conversionService = new DefaultConversionService();

    public StringToEnumConverterFactoryTest() {
        conversionService.addConverterFactory(new StringToEnumConverterFactory());
    }

    @Test
    @DisplayName("비어있는 값이 들어오면 변환이 실패한다")
    void convertFailWithEmptyValue() {
        // given
        String emptyValue = " ";

        // when
        TestValuedEnum converted = conversionService.convert(emptyValue, TestValuedEnum.class);

        // then
        assertThat(converted).isNull();
    }

    @Test
    @DisplayName("Enum 의 name 등 비정상적인 값이 들어오면 변환이 실패한다")
    void convertFailWithInvalidValue() {
        // given
        String name = TestValuedEnum.A.name();

        // when
        TestValuedEnum converted = conversionService.convert(name, TestValuedEnum.class);

        // then
        assertThat(converted).isNull();
    }

    @Test
    @DisplayName("ValuedEnum을 상속한 Enum의 value 필드 값은 string -> enum 변환이 성공한다")
    void convertSuccess() {
        // given
        String enumValue = TestValuedEnum.A.getValue();

        // when
        TestValuedEnum converted = conversionService.convert(enumValue, TestValuedEnum.class);

        // then
        assertThat(converted).isNotNull();
        assertThat(converted.getValue()).isEqualTo(enumValue);
    }
}
