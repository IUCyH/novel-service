package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.common.exception.ServiceException;

import java.util.Map;

public class NovelNotFound extends ServiceException {

    public NovelNotFound(long novelId) {
        super(
                NovelErrorCode.NOVEL_NOT_FOUND,
                Map.of("novelId", novelId)
        );
    }
}
