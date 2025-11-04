package com.iucyh.novelservice.common.exception.user;

import com.iucyh.novelservice.common.exception.ServiceException;

import java.util.Map;

public class UserNotFound extends ServiceException {

    public UserNotFound(String publicId) {
        super(
                UserErrorCode.USER_NOT_FOUND,
                Map.of("userId", publicId)
        );
    }
}
