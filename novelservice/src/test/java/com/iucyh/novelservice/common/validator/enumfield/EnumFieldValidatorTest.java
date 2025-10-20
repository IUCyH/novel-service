package com.iucyh.novelservice.common.validator.enumfield;

import com.iucyh.novelservice.common.validator.enumfield.testsupport.TestEnumFieldDto;
import com.iucyh.novelservice.common.testsupport.TestValuedEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class EnumFieldValidatorTest {

    private final Validator validator;

    public EnumFieldValidatorTest() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    @Test
    @DisplayName("Enum Field 어노테이션이 달린 dto에 Enum의 name 등 비정상적인 값을 넣으면 실패한다")
    void enumFieldValidateFail() {
        // given
        TestEnumFieldDto testEnumFieldDto = new TestEnumFieldDto("test", TestValuedEnum.A.name());

        // when
        Set<ConstraintViolation<TestEnumFieldDto>> violations = validator.validate(testEnumFieldDto);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Enum Field 어노테이션이 달린 dto에 Enum의 value 필드값을 넣으면 검증이 성공한다")
    void enumFieldValidateSuccess() {
        // given
        TestEnumFieldDto testEnumFieldDto = new TestEnumFieldDto("test", TestValuedEnum.A.getValue());

        // when
        Set<ConstraintViolation<TestEnumFieldDto>> violations = validator.validate(testEnumFieldDto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Enum Field 어노테이션이 달린 dto에 null을 넣어도 검증이 성공한다")
    void enumFieldValidateSuccessWithNull() {
        // given
        TestEnumFieldDto testEnumFieldDto = new TestEnumFieldDto("test", null);

        // when
        Set<ConstraintViolation<TestEnumFieldDto>> violations = validator.validate(testEnumFieldDto);

        // then
        assertThat(violations).isEmpty();
    }
}
