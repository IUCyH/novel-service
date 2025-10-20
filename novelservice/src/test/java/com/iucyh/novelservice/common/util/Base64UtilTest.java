package com.iucyh.novelservice.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iucyh.novelservice.common.testsupport.TestObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class Base64UtilTest {

    private final Base64Util base64Util;

    public Base64UtilTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        base64Util = new Base64Util(objectMapper);

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("정상적인 객체를 넣으면 인코딩이 성공한다")
    void encodeSuccess() {
        // given
        TestObject object = new TestObject("test", 20);

        // when
        String encoded = base64Util.encode(object);

        // then
        assertThat(encoded).isNotNull();
        assertThat(encoded).isNotEmpty();
    }

    @Test
    @DisplayName("정상적으로 인코딩이 된 후 디코딩 하면 성공한다")
    void decodeSuccess() {
        // given
        TestObject object = new TestObject("test", 20);
        String encoded = base64Util.encode(object);

        // when
        TestObject decoded = base64Util.decode(encoded, TestObject.class);

        // then
        assertThat(decoded).isNotNull();
        assertThat(decoded.getName()).isEqualTo(object.getName());
        assertThat(decoded.getAge()).isEqualTo(object.getAge());
    }
}
