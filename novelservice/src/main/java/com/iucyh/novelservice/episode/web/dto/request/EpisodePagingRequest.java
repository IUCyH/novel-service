package com.iucyh.novelservice.episode.web.dto.request;

import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.RequestParam;

public record EpisodePagingRequest(

        @RequestParam("last-episode")
        @Positive
        Integer lastEpisode,

        @Range(min = 1, max = 100, message = "Limit must be between 1 and 100")
        Integer limit
) {
    public EpisodePagingRequest {
        if (limit == null) {
            limit = 50;
        }
    }
}
