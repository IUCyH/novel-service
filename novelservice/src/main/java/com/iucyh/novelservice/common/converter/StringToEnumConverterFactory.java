package com.iucyh.novelservice.common.converter;

import com.iucyh.novelservice.common.enumtype.ValuedEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToEnumConverterFactory implements ConverterFactory<String, ValuedEnum> {

    @Override
    public <T extends ValuedEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private static class StringToEnumConverter<T extends ValuedEnum> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToEnumConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (source.isBlank()) {
                return null;
            }

            T[] enumConstants = targetType.getEnumConstants();
            for (T enumConstant : enumConstants) {
                String value = enumConstant.getValue();
                if (value.equals(source)) {
                    return enumConstant;
                }
            }
            return null;
        }
    }
}
