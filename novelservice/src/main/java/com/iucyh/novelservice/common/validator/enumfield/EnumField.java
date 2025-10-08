package com.iucyh.novelservice.common.validator;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumField {

    Class<? extends Enum<?>> target();

    String message() default "Invalid Enum Value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
