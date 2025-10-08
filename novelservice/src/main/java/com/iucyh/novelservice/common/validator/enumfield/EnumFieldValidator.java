package com.iucyh.novelservice.common.validator.enumfield;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumFieldValidator implements ConstraintValidator<EnumField, String> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(EnumField constraintAnnotation) {
        Enum<?>[] enumConstants = constraintAnnotation.enumClass().getEnumConstants();
        acceptedValues = Arrays.stream(enumConstants)
                .flatMap(e -> {
                    Stream.Builder<String> builder = Stream.builder(); // 각 요소별로 반환할 Stream
                    builder.add(e.name().toUpperCase()); // 열거형의 기본 이름은 무조건 추가

                    // 검증하려는 Enum 에서 @JsonValue 등으로 커스텀 직렬화 값을 사용하는 경우 대응
                    try {
                        Field field = e.getClass().getDeclaredField("value"); // 리플렉션으로 해당 Enum 타입의 value 필드 접근 (private 등 접근 지정자 무시)
                        field.setAccessible(true); // 이후 로직이 문제없도록 접근 지정자 무효화

                        Object value = field.get(e); // 각 요소에 해당하는 "value" 필드 가져오기
                        if (value != null) {
                            builder.add(value.toString().toUpperCase()); // 해당 필드의 값이 존재하면 stream에 추가
                        }
                    } catch (NoSuchFieldException | IllegalAccessException ignored) {
                        // "value" 필드가 존재하지 않는 Enum 이라면 무시
                    }

                    return builder.build();
                })
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || acceptedValues.contains(value.toUpperCase());
    }
}
