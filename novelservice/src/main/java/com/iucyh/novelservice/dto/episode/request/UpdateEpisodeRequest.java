package com.iucyh.novelservice.dto.episode.request;

import com.iucyh.novelservice.common.validator.notblank.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import static com.iucyh.novelservice.common.constant.EpisodeConstants.*;

public record UpdateEpisodeRequest(

        @NotBlankIfPresent
        @Size(min = EPISODE_TITLE_LENGTH_MIN, max = EPISODE_TITLE_LENGTH_MAX)
        String title,

        @Size(max = EPISODE_DESC_LENGTH_MAX)
        String description,

        @Size(min = EPISODE_CONTENT_LENGTH_MIN, max = EPISODE_CONTENT_LENGTH_MAX)
        String content
) {}
