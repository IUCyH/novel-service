package com.iucyh.novelservice.novel.service.codec;

import com.iucyh.novelservice.novel.exception.InvalidNovelCursor;
import com.iucyh.novelservice.common.util.Base64Util;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovelCursorBase64Codec {

    private final Base64Util base64Util;

    public String encode(NovelCursor cursor) {
        return base64Util.encode(cursor);
    }

    public <T extends NovelCursor> NovelCursor decode(String cursor, Class<T> type) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }

        try {
            return base64Util.decode(cursor, type);
        } catch (RuntimeException e) {
            throw new InvalidNovelCursor();
        }
    }
}
