package com.iucyh.novelservice.common.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class IpUtil {

    private static final List<String> IP_HEADERS = List.of(
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    );

    private IpUtil() {}

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!isEmpty(ip)) {
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
            return ip;
        }

        return IP_HEADERS.stream()
                .map(request::getHeader)
                .filter(v -> !isEmpty(v))
                .findFirst()
                .orElse(request.getRemoteAddr());
    }

    private static boolean isEmpty(String ip) {
        return ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip);
    }
}
