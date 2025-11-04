package com.iucyh.novelservice.episode.web.dto.request;

import com.iucyh.novelservice.common.validator.notblank.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.EPISODE_CONTENT_LENGTH_MAX;
import static com.iucyh.novelservice.episode.constant.EpisodeConstants.EPISODE_CONTENT_LENGTH_MIN;

public record UpdateEpisodeDetailRequest(

        @NotBlankIfPresent
        @Size(min = EPISODE_CONTENT_LENGTH_MIN, max = EPISODE_CONTENT_LENGTH_MAX)
        String content
) {}
