package com.iucyh.novelservice.controller.novel;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.SuccessDto;
import com.iucyh.novelservice.dto.novel.*;
import com.iucyh.novelservice.service.novel.NovelQueryService;
import com.iucyh.novelservice.service.novel.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        PagingResultDto<NovelDto> results = novelQueryService.findNovels(requestDto);
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
    public SuccessDto<NovelDto> createNovel(
            @Valid @RequestBody CreateNovelDto createNovelDto
    ) {
        NovelDto newNovel = novelService.createNovel(createNovelDto);
        return new SuccessDto<>(newNovel);
    }

    @PatchMapping("/{novelId}")
    public SuccessDto<NovelDto> updateNovel(
            @PathVariable long novelId,
            @Valid @RequestBody UpdateNovelDto updateNovelDto
    ) {
        NovelDto updatedNovel = novelService.updateNovel(1, novelId, updateNovelDto);
        return new SuccessDto<>(updatedNovel);
    }

    @PostMapping("/{novelId}/likes")
    public SuccessDto<UpdateLikeCountDto> addLikeCount(
            @PathVariable long novelId
    ) {
        UpdateLikeCountDto result = novelService.addLikeCount(1, novelId);
        return new SuccessDto<>(result);
    }

    @DeleteMapping("/{novelId}/likes")
    public SuccessDto<UpdateLikeCountDto> removeLikeCount(
            @PathVariable long novelId
    ) {
        UpdateLikeCountDto result = novelService.removeLikeCount(1, novelId);
        return new SuccessDto<>(result);
    }

    @DeleteMapping("/{novelId}")
    public SuccessDto<Void> deleteNovel(
            @PathVariable long novelId
    ) {
        novelService.deleteNovel(1, novelId);
        return SuccessDto.empty();
    }
}
