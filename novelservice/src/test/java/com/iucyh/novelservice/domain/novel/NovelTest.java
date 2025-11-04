package com.iucyh.novelservice.domain.novel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class NovelTest {

    @Test
    @DisplayName("텍스트 관련 필드 업데이트 메서드의 모든 인자에 null을 전달하면 업데이트 되지 않는다")
    void textFieldsUpdateFailWithNulls() {
        // given
        Novel novel = Novel.of("old", "old desc", NovelCategory.ETC);

        // when
        novel.updateTextMetaData(null, null);

        // then
        assertThat(novel.getTitle()).isEqualTo("old");
        assertThat(novel.getDescription()).isEqualTo("old desc");
    }

    @Test
    @DisplayName("텍스트 관련 필드 업데이트 메서드에서 제목에 빈 문자열을 전달하면 업데이트 되지 않는다")
    void titleUpdateFailWithEmptyTitle() {
        // given
        Novel novel = Novel.of("old", "test desc", NovelCategory.ETC);

        // when
        novel.updateTextMetaData(" ", null);

        // then
        assertThat(novel.getTitle()).isEqualTo("old");
    }

    @Test
    @DisplayName("텍스트 관련 필드 업데이트 메서드에 정상 값들을 전달하면 업데이트 된다")
    void textFieldsUpdateSuccess() {
        // given
        Novel novel = Novel.of("old", "old desc", NovelCategory.ETC);

        // when
        novel.updateTextMetaData("new", "new desc");

        // then
        assertThat(novel.getTitle()).isEqualTo("new");
        assertThat(novel.getDescription()).isEqualTo("new desc");
    }

    @Test
    @DisplayName("카테고리 업데이트 메서드에 null을 전달하면 업데이트 되지 않는다")
    void categoryUpdateFailWithNull() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);

        // when
        novel.updateCategory(null);

        // then
        assertThat(novel.getCategory()).isEqualTo(NovelCategory.ETC);
    }

    @Test
    @DisplayName("카테고리 업데이트 메서드에 정상값을 전달하면 업데이트 된다")
    void categoryUpdateSuccess() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);

        // when
        novel.updateCategory(NovelCategory.SF);

        // then
        assertThat(novel.getCategory()).isEqualTo(NovelCategory.SF);
    }

    @Test
    @DisplayName("좋아요 수 증가 메서드에 0보다 작은 값을 전달하면 증가하지 않는다")
    void likeCountDoesntIncrease() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);

        // when
        novel.addLikes(0);
        novel.addLikes(-1);

        // then
        assertThat(novel.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 수 증가 메서드에 0보다 큰 값을 전달하면 증가한다")
    void likeCountIncreases() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);

        // when
        novel.addLikes(1);

        // then
        assertThat(novel.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 수 감소 메서드에 0보다 작은 값을 전달하면 감소하지 않는다")
    void likeCountDoesntDecrease() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);
        novel.addLikes(1);

        // when
        novel.removeLikes(0);
        novel.removeLikes(-1);

        // then
        assertThat(novel.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 수 감소 메서드에 0보다 큰 값을 전달하면 감소한다")
    void likeCountDecreases() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);
        novel.addLikes(1);

        // when
        novel.removeLikes(1);

        // then
        assertThat(novel.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 수 감소 메서드에 전달한 값이 기존 좋아요 수보다 더 커도 음수가 되지 않는다")
    void likeCountCannotBeNegative() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);
        novel.addLikes(2);

        // when
        novel.removeLikes(3);

        // then
        assertThat(novel.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("조회수 증가 메서드에 0보다 작은 값을 전달하면 증가하지 않는다")
    void viewCountDoesntIncrease() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);

        // when
        novel.addViews(0);
        novel.addViews(-1);

        // then
        assertThat(novel.getTotalViewCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("조회수 증가 메서드에 0보다 큰 값을 전달하면 증가한다")
    void viewCountIncreases() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);

        // when
        novel.addViews(1);

        // then
        assertThat(novel.getTotalViewCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("마지막 회차 등록일 업데이트 메서드에 null을 전달하면 메서드 호출 시점의 시간으로 설정된다")
    void lastEpisodeAtUpdateWithNow() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);
        LocalDateTime startTime = LocalDateTime.now();

        // when
        novel.updateLastEpisodeAt(null);

        // then
        assertThat(novel.getLastEpisodeAt()).isNotNull();
        assertThat(novel.getLastEpisodeAt()).isAfterOrEqualTo(startTime);
    }

    @Test
    @DisplayName("마지막 회차 등록일 업데이트 메서드에 시간을 전달하면 그 시간으로 업데이트 된다")
    void lastEpisodeAtUpdateWithSpecificTime() {
        // given
        Novel novel = Novel.of("test", "test desc", NovelCategory.ETC);
        LocalDateTime startTime = LocalDateTime.now();

        // when
        novel.updateLastEpisodeAt(startTime);

        // then
        assertThat(novel.getLastEpisodeAt()).isNotNull();
        assertThat(novel.getLastEpisodeAt()).isEqualTo(startTime);
    }
}
