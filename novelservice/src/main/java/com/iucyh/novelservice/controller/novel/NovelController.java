package com.iucyh.novelservice.controller.novel;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.IdDto;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.SuccessDto;
import com.iucyh.novelservice.dto.novel.CreateNovelDto;
import com.iucyh.novelservice.dto.novel.NovelDto;
import com.iucyh.novelservice.dto.novel.NovelPagingRequestDto;
import com.iucyh.novelservice.dto.novel.UpdateNovelDto;
import com.iucyh.novelservice.service.novel.NovelQueryService;
import com.iucyh.novelservice.service.novel.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/novels")
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;
    private final NovelQueryService novelQueryService;

    @GetMapping
    public SuccessDto<PagingResultDto<NovelDto>> getNovels(
            @Valid @ModelAttribute NovelPagingRequestDto requestDto
    ) {
        PagingResultDto<NovelDto> results = novelQueryService.findNewNovels(requestDto);
        return new SuccessDto<>(results);
    }

    @GetMapping("/new")
    public SuccessDto<PagingResultDto<NovelDto>> getNewNovels(
            @Valid @ModelAttribute NovelPagingRequestDto requestDto
    ) {
        PagingResultDto<NovelDto> results = novelQueryService.findNewNovels(requestDto);
        return new SuccessDto<>(results);
    }

    @GetMapping("/category/{category}")
    public SuccessDto<PagingResultDto<NovelDto>> getNovelsByCategory(
            @PathVariable NovelCategory category,
            @Valid @ModelAttribute NovelPagingRequestDto requestDto
    ) {
        PagingResultDto<NovelDto> results = novelQueryService.findNovelsByCategory(requestDto, category);
        return new SuccessDto<>(results);
    }

    @PostMapping
    public SuccessDto<IdDto> createNovel(
            @Valid @RequestBody CreateNovelDto createNovelDto
    ) {
        IdDto idDto = novelService.createNovel(createNovelDto);
        return new SuccessDto<>(idDto);
    }

    @PatchMapping("/{publicId}")
    public SuccessDto<Void> updateNovel(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdateNovelDto updateNovelDto
    ) {
        novelService.updateNovel(1, publicId, updateNovelDto);
        return SuccessDto.empty();
    }

    @PostMapping("/{publicId}/likes")
    public SuccessDto<Void> addLikeCount(
            @PathVariable UUID publicId
    ) {
        novelService.addLikeCount(1, publicId);
        return SuccessDto.empty();
    }

    @DeleteMapping("/{publicId}/likes")
    public SuccessDto<Void> removeLikeCount(
            @PathVariable UUID publicId
    ) {
        novelService.removeLikeCount(1, publicId);
        return SuccessDto.empty();
    }

    @DeleteMapping("/{publicId}")
    public SuccessDto<Void> deleteNovel(
            @PathVariable UUID publicId
    ) {
        novelService.deleteNovel(1, publicId);
        return SuccessDto.empty();
    }
}
