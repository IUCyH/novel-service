package com.iucyh.novelservice.common.validator.enumfield;

import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumFieldValidator implements ConstraintValidator<EnumField, String> {

    private Set<String> enumValues;

    @Override
    public void initialize(EnumField constraintAnnotation) {
        ValuedEnum[] enumConstants = constraintAnnotation.enumClass().getEnumConstants();
        enumValues = Arrays.stream(enumConstants)
                .map(ValuedEnum::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || enumValues.contains(value.trim());
    }
}
