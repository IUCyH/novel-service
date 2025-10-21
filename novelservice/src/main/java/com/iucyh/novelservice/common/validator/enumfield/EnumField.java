package com.iucyh.novelservice.common.validator.enumfield;

import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnum;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * <p>String 타입으로 들어온 값이 enumClass 에 정의한 Enum 으로 변환 가능한지 검증</p>
 * <b>주의: 검증 대상 값이 null 인 경우 무시 -> 필요 시 NotNull 등의 null 검증 어노테이션과 같이 쓰기를 권장</b>
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumFieldValidator.class)
public @interface EnumField {

    Class<? extends ValuedEnum> enumClass();

    String message() default "Invalid enum value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
