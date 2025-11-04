package com.iucyh.novelservice.common.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

public class StringToUUIDConverter implements Converter<String, UUID> {

    @Override
    public UUID convert(String source) {
        if (source.isBlank() || source.length() != 32) {
            return null;
        }

        StringBuilder sb = new StringBuilder(source);
        sb.insert(8, '-');
        sb.insert(13, '-');
        sb.insert(18, '-');
        sb.insert(23, '-');

        return UUID.fromString(sb.toString());
    }
}
