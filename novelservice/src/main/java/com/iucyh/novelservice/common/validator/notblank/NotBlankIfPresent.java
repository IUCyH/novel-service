package com.iucyh.novelservice.common.validator.notblank;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * <p>대상 문자열이 비어있지 않고, 공백으로만 채워지지도 않았는지 검증</p>
 * <b>주의: 검증 대상 값이 null 인 경우 무시 -> null 체크까지 필요하면 NotBlank 등의 기본 검증기 사용 권장</b>
 * <p>예) 검증 실패 케이스: "" (empty), " " (blank) </p>
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
public @interface NotBlankIfPresent {

    String message() default "Value cannot be blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
