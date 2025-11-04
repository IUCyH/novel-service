package com.iucyh.novelservice.novel.web.dto.mapper;

import com.iucyh.novelservice.common.dto.response.PagingResponse;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelResponse;

import java.util.List;

public class NovelResponseMapper {

    private NovelResponseMapper() {}

    public static NovelResponse toNovelResponse(Novel novel) {
        return new NovelResponse(
                novel.getId(),
                novel.getTitle(),
                novel.getDescription(),
                novel.getLikeCount(),
                novel.getTotalViewCount(),
                novel.getCategory(),
                novel.getLastEpisodeAt(),
                novel.getCreatedAt()
        );
    }

    public static NovelLikeCountResponse toNovelLikeCountResponse(int likeCount) {
        return new NovelLikeCountResponse(likeCount);
    }

    public static PagingResponse<NovelResponse> toPagingResultDto(List<NovelResponse> novels, long totalCount, String encodedCursor) {
        return new PagingResponse<>(totalCount, encodedCursor, novels);
    }
}
