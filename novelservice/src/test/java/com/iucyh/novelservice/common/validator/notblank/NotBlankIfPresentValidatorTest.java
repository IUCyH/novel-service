package com.iucyh.novelservice.common.validator.notblank;

import com.iucyh.novelservice.common.validator.notblank.testsupport.TestNotBlankDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class NotBlankIfPresentValidatorTest {

    private final Validator validator;

    public NotBlankIfPresentValidatorTest() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    @Test
    @DisplayName("빈 값이 들어오면 검증이 실패한다")
    void notBlankValidateFailedWithEmptyValue() {
        // given
        TestNotBlankDto dto = new TestNotBlankDto("");

        // when
        Set<ConstraintViolation<TestNotBlankDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("공백이 들어오면 검증이 실패한다")
    void notBlankValidateFailedWithBlankValue() {
        // given
        TestNotBlankDto dto = new TestNotBlankDto(" ");

        // when
        Set<ConstraintViolation<TestNotBlankDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("비어있지 않고 공백만 있지도 않은 값이면 검증이 성공한다")
    void notBlankValidateSuccess() {
        // given
        TestNotBlankDto dto = new TestNotBlankDto("valid value");

        // when
        Set<ConstraintViolation<TestNotBlankDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("값이 null 이어도 검증이 성공한다")
    void notBlankValidateSuccessWithNullValue() {
        // given
        TestNotBlankDto dto = new TestNotBlankDto(null);

        // when
        Set<ConstraintViolation<TestNotBlankDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isEmpty();
    }
}
