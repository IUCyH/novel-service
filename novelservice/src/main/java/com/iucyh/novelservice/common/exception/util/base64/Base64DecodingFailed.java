package com.iucyh.novelservice.common.exception.util.base64;

public class Base64DecodingFailed extends RuntimeException {

    public Base64DecodingFailed(Throwable cause) {
        super("Failed to decode from base64", cause);
    }
}
