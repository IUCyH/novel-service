package com.iucyh.novelservice.common.util;

import org.apache.tomcat.util.http.parser.TE;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class IpUtilTest {

    private static final String TEST_IP = "192.168.0.1";

    private static Stream<String> ipHeaders() {
        return Stream.of(
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        );
    }

    @ParameterizedTest
    @MethodSource("ipHeaders")
    @DisplayName("각 헤더별로 IP 조회가 성공한다")
    void returnsIpWithEachHeader(String header) {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(header, TEST_IP);

        // when
        String ip = IpUtil.getIpAddr(request);

        // then
        assertThat(ip).isNotNull();
        assertThat(ip).isEqualTo(TEST_IP);
    }

    @Test
    @DisplayName("X-Forwarded-For 헤더가 여러개 존재할 때 첫번째 값이 반환된다")
    void returnsIpWithMultipleXForwardedFor() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", TEST_IP);
        request.addHeader("X-Forwarded-For", "0.0.0.0");

        // when
        String ip = IpUtil.getIpAddr(request);

        // then
        assertThat(ip).isNotNull();
        assertThat(ip).isEqualTo(TEST_IP);
    }

    @Test
    @DisplayName("어떤 헤더도 제공되지 않았을 때 getRemoteAddr 의 값이 반환된다")
    void returnsIpWithGetRemoteAddr() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        String ip = IpUtil.getIpAddr(request);

        // then
        assertThat(ip).isNotNull();
        assertThat(ip).isEqualTo("127.0.0.1"); // getRemoteAddr 기본값
    }
}
