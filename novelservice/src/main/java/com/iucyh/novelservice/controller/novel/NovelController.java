package com.iucyh.novelservice.controller.novel;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.SuccessDto;
import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.request.EpisodePagingRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeDetailRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.response.EpisodeDetailResponse;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;
import com.iucyh.novelservice.dto.novel.*;
import com.iucyh.novelservice.service.episode.EpisodeQueryService;
import com.iucyh.novelservice.service.episode.EpisodeService;
import com.iucyh.novelservice.service.novel.NovelQueryService;
import com.iucyh.novelservice.service.novel.NovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/novels")
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;
    private final EpisodeService episodeService;
    private final NovelQueryService novelQueryService;
    private final EpisodeQueryService episodeQueryService;

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

    @GetMapping("/{novelId}/episodes")
    public SuccessDto<PagingResultDto<EpisodeResponse>> getEpisodes(
            @PathVariable long novelId,
            @Valid @ModelAttribute EpisodePagingRequest request
    ) {
        PagingResultDto<EpisodeResponse> result = episodeQueryService.findEpisodesByNovel(novelId, request);
        return new SuccessDto<>(result);
    }

    @GetMapping("/{novelId}/episodes/{episodeNumber}")
    public SuccessDto<EpisodeDetailResponse> getEpisodeDetail(
            @PathVariable long novelId,
            @PathVariable int episodeNumber
    ) {
        EpisodeDetailResponse result = episodeQueryService.findEpisodeDetail(novelId, episodeNumber);
        return new SuccessDto<>(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessDto<NovelDto> createNovel(
            @Valid @RequestBody CreateNovelDto createNovelDto
    ) {
        NovelDto newNovel = novelService.createNovel(createNovelDto);
        return new SuccessDto<>(newNovel);
    }

    @PostMapping("/{novelId}/episodes")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessDto<EpisodeResponse> createEpisode(
            @PathVariable long novelId,
            @Valid @RequestBody CreateEpisodeRequest request
    ) {
        EpisodeResponse result = episodeService.createEpisode(novelId, request);
        return new SuccessDto<>(result);
    }

    @PatchMapping("/{novelId}")
    public SuccessDto<NovelDto> updateNovel(
            @PathVariable long novelId,
            @Valid @RequestBody UpdateNovelDto updateNovelDto
    ) {
        NovelDto updatedNovel = novelService.updateNovel(1, novelId, updateNovelDto);
        return new SuccessDto<>(updatedNovel);
    }

    @PatchMapping("/{novelId}/episodes/{episodeId}")
    public SuccessDto<EpisodeResponse> updateEpisode(
            @PathVariable long novelId,
            @PathVariable long episodeId,
            @Valid @RequestBody UpdateEpisodeRequest request
    ) {
        EpisodeResponse result = episodeService.updateEpisode(novelId, episodeId, request);
        return new SuccessDto<>(result);
    }

    @PatchMapping("/{novelId}/episodes/{episodeId}/detail")
    public SuccessDto<EpisodeDetailResponse> updateEpisodeDetail(
            @PathVariable long novelId,
            @PathVariable long episodeId,
            @Valid @RequestBody UpdateEpisodeDetailRequest request
    ) {
        EpisodeDetailResponse result = episodeService.updateEpisodeDetail(novelId, episodeId, request);
        return new SuccessDto<>(result);
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

    @DeleteMapping("/{novelId}/episodes/{episodeId}")
    public SuccessDto<Void> deleteEpisode(
            @PathVariable long novelId,
            @PathVariable long episodeId
    ) {
        episodeService.deleteEpisode(novelId, episodeId);
        return SuccessDto.empty();
    }
}
