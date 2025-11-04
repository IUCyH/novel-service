package com.iucyh.novelservice.episode.web.dto.request;

import com.iucyh.novelservice.common.validator.notblank.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.*;

public record UpdateEpisodeRequest(

        @NotBlankIfPresent
        @Size(min = EPISODE_TITLE_LENGTH_MIN, max = EPISODE_TITLE_LENGTH_MAX)
        String title,

        @Size(max = EPISODE_DESC_LENGTH_MAX)
        String description
) {}
