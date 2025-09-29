package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.repository.novel.query.cursor.*;

public enum NovelSortType {

    POPULAR {
        @Override
        public Class<? extends NovelCursor> getSupportedCursorClass() {
            return NovelPopularCursor.class;
        }
    },
    LAST_UPDATE {
        @Override
        public Class<? extends NovelCursor> getSupportedCursorClass() {
            return NovelLastUpdateCursor.class;
        }
    },
    VIEW_COUNT {
        @Override
        public Class<? extends NovelCursor> getSupportedCursorClass() {
            return NovelViewCountCursor.class;
        }
    },
    LIKE_COUNT {
        @Override
        public Class<? extends NovelCursor> getSupportedCursorClass() {
            return NovelLikeCountCursor.class;
        }
    };

    public abstract Class<? extends NovelCursor> getSupportedCursorClass();
}
