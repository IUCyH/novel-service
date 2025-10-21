package com.iucyh.novelservice.dto.novel;

import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.PagingResultDto;

import java.util.List;

public class NovelDtoMapper {

    private NovelDtoMapper() {}

    public static NovelDto toNovelDto(Novel novel) {
        return new NovelDto(
                novel.getPublicIdToString(),
                novel.getTitle(),
                novel.getDescription(),
                novel.getLikeCount(),
                novel.getTotalViewCount(),
                novel.getCategory(),
                novel.getLastEpisodeAt(),
                novel.getCreatedAt()
        );
    }

    public static PagingResultDto<NovelDto> toPagingResultDto(List<NovelDto> novels, long totalCount, String encodedCursor) {
        return new PagingResultDto<>(totalCount, encodedCursor, novels);
    }

    public static Novel toNovelEntity(CreateNovelDto novelDto) {
        NovelCategory category = NovelCategory.of(novelDto.getCategory());
        return Novel.of(novelDto.getTitle(), novelDto.getDescription(), category);
    }
}
