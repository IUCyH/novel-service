package com.iucyh.novelservice.episode.exception.errorcode;

import com.iucyh.novelservice.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EpisodeErrorCode implements ErrorCode {

    EPISODE_NOT_FOUND(HttpStatus.NOT_FOUND, "EPISODE-4041", "Episode not found with this id");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
