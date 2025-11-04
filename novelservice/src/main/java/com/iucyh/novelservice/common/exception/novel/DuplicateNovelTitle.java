package com.iucyh.novelservice.common.exception.novel;

import com.iucyh.novelservice.common.exception.ServiceException;

import java.util.Map;

public class DuplicateNovelTitle extends ServiceException {

    public DuplicateNovelTitle(String title) {
        super(
                NovelErrorCode.DUPLICATE_TITLE,
                Map.of("title", title)
        );
    }
}
