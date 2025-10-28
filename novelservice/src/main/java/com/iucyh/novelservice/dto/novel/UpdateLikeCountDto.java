package com.iucyh.novelservice.dto.novel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateLikeCountDto {

    private final int likeCount;
}
