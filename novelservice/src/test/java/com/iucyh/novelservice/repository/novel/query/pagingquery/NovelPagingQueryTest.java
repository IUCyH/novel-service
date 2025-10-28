package com.iucyh.novelservice.repository.novel.query.pagingquery;

import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.iucyh.novelservice.repository.novel.query.cursor.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class NovelPagingQueryTest {

    private final Map<NovelSortType, NovelPagingQuery> pagingQueryMap;

    @Autowired
    public NovelPagingQueryTest(List<NovelPagingQuery> pagingQueries) {
        this.pagingQueryMap = pagingQueries.stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                NovelPagingQuery::getSupportedSortType,
                                Function.identity()
                        )
                );
    }

    @ParameterizedTest
    @EnumSource(NovelSortType.class)
    @DisplayName("각 정렬 기준별로 대응되는 페이징 전략이 존재함")
    void everySortTypeHasPagingQuery(NovelSortType sortType) {
        assertThat(pagingQueryMap.containsKey(sortType))
                .withFailMessage("There's no paging query for: " + sortType.name())
                .isTrue();
    }

    @ParameterizedTest
    @EnumSource(NovelSortType.class)
    @DisplayName("각 정렬 기준이 지원하는 커서 클래스가 기대값과 일치한다")
    void everySortTypeSupportsCorrectCursorClass(NovelSortType sortType) {
        Class<? extends NovelCursor> expectedCursor = switch (sortType) {
            case POPULAR -> NovelPopularCursor.class;
            case LAST_UPDATE -> NovelLastUpdateCursor.class;
            case LIKE_COUNT -> NovelLikeCountCursor.class;
            case VIEW_COUNT -> NovelViewCountCursor.class;
        };
        assertThat(sortType.getSupportedCursorClass())
                .isEqualTo(expectedCursor);
    }

    @ParameterizedTest
    @EnumSource(NovelSortType.class)
    @DisplayName("각 전략이 지원하는 정렬 기준이 기대값과 일치함")
    void everyPagingQuerySupportsCorrectSortType(NovelSortType expectedSortType) {
        NovelPagingQuery pagingQuery = switch (expectedSortType) {
            case POPULAR -> new NovelPopularPagingQuery();
            case LAST_UPDATE -> new NovelLastUpdatePagingQuery();
            case LIKE_COUNT -> new NovelLikeCountPagingQuery();
            case VIEW_COUNT -> new NovelViewCountPagingQuery();
        };
        assertThat(pagingQuery.getSupportedSortType())
                .isEqualTo(expectedSortType);
    }
}
