package com.iucyh.novelservice.common.exception.util.base64;

public class Base64EncodingFailed extends RuntimeException {

    public Base64EncodingFailed(Throwable cause) {
        super("Failed to encode to base64", cause);
    }
}
