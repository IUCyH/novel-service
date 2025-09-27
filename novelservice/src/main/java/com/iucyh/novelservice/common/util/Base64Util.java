package com.iucyh.novelservice.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iucyh.novelservice.common.exception.util.base64.Base64DecodingFailed;
import com.iucyh.novelservice.common.exception.util.base64.Base64EncodingFailed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class Base64Util {

    private final ObjectMapper objectMapper;

    public String encode(Object data) throws Base64EncodingFailed {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(data);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (JsonProcessingException e) {
            throw new Base64EncodingFailed(e);
        }
    }

    public <T> T decode(String encodedValue, Class<T> type) throws Base64DecodingFailed {
        try {
            byte[] bytes = Base64.getDecoder().decode(encodedValue);
            return objectMapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new Base64DecodingFailed(e);
        }
    }
}
