package com.iucyh.novelservice.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class Base64Util {

    private final ObjectMapper objectMapper;

    public String encode(Object data) throws JsonProcessingException {
        byte[] bytes = objectMapper.writeValueAsBytes(data);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public <T> T decode(String encodedValue, Class<T> type) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(encodedValue);
        return objectMapper.readValue(bytes, type);
    }
}
