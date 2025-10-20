package com.iucyh.novelservice.domain.novel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class NovelPeriodViewTest {

    @Test
    @DisplayName("조회수 증가 메서드에 0보다 작은 값을 전달하면 증가하지 않는다")
    void viewCountDoesntIncrease() {
        // given
        NovelPeriodView novelPeriodView = new NovelPeriodView();

        // when
        novelPeriodView.addViews(0);
        novelPeriodView.addViews(-1);

        // then
        assertThat(novelPeriodView.getViewCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("조회수 증가 메서드에 0보다 큰 값을 전달하면 증가한다")
    void viewCountIncreases() {
        // given
        NovelPeriodView novelPeriodView = new NovelPeriodView();

        // when
        novelPeriodView.addViews(1);

        // then
        assertThat(novelPeriodView.getViewCount()).isEqualTo(1);
    }
}
