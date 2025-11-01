package com.iucyh.novelservice.dto.episode.request;

import com.iucyh.novelservice.common.validator.notblank.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import static com.iucyh.novelservice.common.constant.EpisodeConstants.EPISODE_CONTENT_LENGTH_MAX;
import static com.iucyh.novelservice.common.constant.EpisodeConstants.EPISODE_CONTENT_LENGTH_MIN;

public record UpdateEpisodeDetailRequest(

        @NotBlankIfPresent
        @Size(min = EPISODE_CONTENT_LENGTH_MIN, max = EPISODE_CONTENT_LENGTH_MAX)
        String content
) {}
