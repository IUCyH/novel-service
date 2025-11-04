package com.iucyh.novelservice.novel.enumtype;

import com.fasterxml.jackson.annotation.JsonValue;
import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnum;
import com.iucyh.novelservice.common.enumtype.valuedenum.ValuedEnumHelper;
import com.iucyh.novelservice.novel.repository.query.cursor.*;

public enum NovelSortType implements ValuedEnum {

    POPULAR("popular") {
        @Override
        public Class<? extends NovelCursor> getSupportedCursorClass() {
            return NovelPopularCursor.class;
        }
    },
    LAST_UPDATE("last-update") {
        @Override
        public Class<? extends NovelCursor> getSupportedCursorClass() {
            return NovelLastUpdateCursor.class;
        }
    },
    VIEW_COUNT("view-count") {
        @Override
        public Class<? extends NovelCursor> getSupportedCursorClass() {
            return NovelViewCountCursor.class;
        }
    },
    LIKE_COUNT("like-count") {
        @Override
        public Class<? extends NovelCursor> getSupportedCursorClass() {
            return NovelLikeCountCursor.class;
        }
    };

    private final String value;
    
    public abstract Class<? extends NovelCursor> getSupportedCursorClass();
    
    NovelSortType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    public static NovelSortType of(String value) {
        return ValuedEnumHelper.fromValue(value, NovelSortType.class);
    }
}
