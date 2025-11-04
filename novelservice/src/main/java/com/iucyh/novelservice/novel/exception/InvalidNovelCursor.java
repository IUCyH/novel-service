package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.common.exception.ServiceException;

public class InvalidNovelCursor extends ServiceException {

    public InvalidNovelCursor() {
        super(NovelErrorCode.INVALID_CURSOR);
    }
}
