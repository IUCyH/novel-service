package com.iucyh.novelservice.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {

    private IpUtil() {}

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!isEmpty(ip)) {
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
            return ip;
        }

        if (isEmpty(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (!isEmpty(ip)) {
                return ip;
            }
        }

        if (isEmpty(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            if (!isEmpty(ip)) {
                return ip;
            }
        }

        if (isEmpty(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            if (!isEmpty(ip)) {
                return ip;
            }
        }

        if (isEmpty(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (!isEmpty(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr() == null ? "0.0.0.0" : request.getRemoteAddr();
    }

    private static boolean isEmpty(String ip) {
        return ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip);
    }
}
