package com.iucyh.novelservice.common.exception.novel;

import com.iucyh.novelservice.common.exception.ServiceException;

import java.util.Map;

public class NovelNotFound extends ServiceException {

    public NovelNotFound(String publicId) {
        super(
                NovelErrorCode.NOVEL_NOT_FOUND,
                "Novel not found with this public id",
                Map.of("novelId", publicId)
        );
    }
}
