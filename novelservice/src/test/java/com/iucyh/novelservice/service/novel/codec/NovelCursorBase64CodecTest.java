package com.iucyh.novelservice.service.novel.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iucyh.novelservice.common.exception.novel.InvalidNovelCursor;
import com.iucyh.novelservice.common.util.Base64Util;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelPopularCursor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class NovelCursorBase64CodecTest {

    private final NovelCursorBase64Codec codec;

    public NovelCursorBase64CodecTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        Base64Util base64Util = new Base64Util(objectMapper);
        codec = new NovelCursorBase64Codec(base64Util);
    }

    @Test
    @DisplayName("디코딩 시 인자로 넘긴 커서가 null 이라면 결과값은 null 이 된다")
    void decodeWithNullCursorReturnNull() {
        // when
        NovelCursor result = codec.decode(null, NovelPopularCursor.class);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("디코딩 시 인자로 넘긴 커서가 빈 값이라면 결과값은 null 이 된다")
    void decodeWithEmptyCursorReturnNull() {
        // when
        NovelCursor result = codec.decode(" ", NovelPopularCursor.class);

        // then
        assertThat(result).isNull();
    }
    
    @Test
    @DisplayName("디코딩 실패 시 Novel 도메인 예외가 발생한다")
    void decodeFailWillThrowsDomainException() {
        // given
        String wrongCursor = "wrong cursor";
        
        // then
        assertThatThrownBy(() -> codec.decode(wrongCursor, NovelPopularCursor.class))
                .isInstanceOf(InvalidNovelCursor.class);
    }
}
