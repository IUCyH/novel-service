package com.iucyh.novelservice.domain.episode;

import com.iucyh.novelservice.testsupport.testfactory.episode.EpisodeEntityTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class EpisodeTest {

    private static Stream<Arguments> invalidTextMetadata() {
        return Stream.of(
                Arguments.of("제목, 설명 둘다 null 일 때", null, null),
                Arguments.of("제목이 비어있을 때", " ", null)
        );
    }

    private static Stream<Arguments> invalidContents() {
        return Stream.of(
                Arguments.of("본문이 null 일 때", null),
                Arguments.of("본문이 비어있을 때", " ")
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("invalidTextMetadata")
    @DisplayName("텍스트 관련 필드 업데이트에서 잘못된 인자들은 무시된다")
    void failedUpdateTextMetaData(String testDesc, String title, String desc) {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisode();
        String originalTitle = episode.getTitle();
        String originalDesc = episode.getDescription();

        // when
        episode.updateTextMetaData(title, desc);

        // then
        assertThat(episode.getTitle()).isNotNull();
        assertThat(episode.getTitle()).isNotBlank();
        assertThat(episode.getDescription()).isNotNull();

        assertThat(episode.getTitle()).isEqualTo(originalTitle);
        assertThat(episode.getDescription()).isEqualTo(originalDesc);
    }

    @Test
    @DisplayName("텍스트 관련 필드 업데이트에서 정상적인 인자가 전달되면 업데이트 된다")
    void successTextMetaData() {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisode();
        String newTitle = "new title";
        String newDesc = "new desc";

        // when
        episode.updateTextMetaData(newTitle, newDesc);

        // then
        assertThat(episode.getTitle()).isEqualTo(newTitle);
        assertThat(episode.getDescription()).isEqualTo(newDesc);
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("invalidContents")
    @DisplayName("본문 업데이트에서 잘못된 인자는 무시된다")
    void failedUpdateContent(String testDesc, String content) {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisode();
        String originalContent = episode.getContent();

        // when
        episode.updateContent(content);

        // then
        assertThat(episode.getContent()).isNotNull();
        assertThat(episode.getContent()).isNotBlank();

        assertThat(episode.getContent()).isEqualTo(originalContent);
    }

    @Test
    @DisplayName("본문 업데이트에 올바른 인자가 전달되면 업데이트 된다")
    void successUpdateContent() {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisode();
        String newContent = "new content";

        // when
        episode.updateContent(newContent);

        // then
        assertThat(episode.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("조회수 증가 메서드에서 0 보다 작은 값이 전달되면 무시된다")
    void failedAddViewsWithNegativeValue() {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisode();

        // when
        episode.addViews(0);
        episode.addViews(-1);

        // then
        assertThat(episode.getViewCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("조회수 증가 메서드에서 올바른 값이 전달되면 업데이트 된다")
    void successAddViews() {
        // given
        Episode episode = EpisodeEntityTestFactory.defaultEpisode();

        // when
        episode.addViews(1);

        // then
        assertThat(episode.getViewCount()).isEqualTo(1);
    }
}
