package com.iucyh.novelservice.novel.controller;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.SuccessDto;
import com.iucyh.novelservice.dto.episode.request.CreateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.request.EpisodePagingRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeDetailRequest;
import com.iucyh.novelservice.dto.episode.request.UpdateEpisodeRequest;
import com.iucyh.novelservice.dto.episode.response.EpisodeDetailResponse;
import com.iucyh.novelservice.dto.episode.response.EpisodeResponse;
import com.iucyh.novelservice.novel.dto.request.CreateNovelRequest;
import com.iucyh.novelservice.novel.dto.request.NovelPagingRequest;
import com.iucyh.novelservice.novel.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.dto.request.UpdateNovelRequest;
import com.iucyh.novelservice.novel.dto.response.NovelResponse;
import com.iucyh.novelservice.service.episode.EpisodeQueryService;
import com.iucyh.novelservice.service.episode.EpisodeService;
import com.iucyh.novelservice.novel.service.NovelQueryService;
import com.iucyh.novelservice.novel.service.NovelService;
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
    public SuccessDto<PagingResultDto<NovelResponse>> getNovels(
            @Valid @ModelAttribute NovelPagingRequest requestDto
    ) {
        PagingResultDto<NovelResponse> results = novelQueryService.findNovels(requestDto);
        return new SuccessDto<>(results);
    }

    @GetMapping("/new")
    public SuccessDto<PagingResultDto<NovelResponse>> getNewNovels(
            @Valid @ModelAttribute NovelPagingRequest requestDto
    ) {
        PagingResultDto<NovelResponse> results = novelQueryService.findNewNovels(requestDto);
        return new SuccessDto<>(results);
    }

    @GetMapping("/category/{category}")
    public SuccessDto<PagingResultDto<NovelResponse>> getNovelsByCategory(
            @PathVariable NovelCategory category,
            @Valid @ModelAttribute NovelPagingRequest requestDto
    ) {
        PagingResultDto<NovelResponse> results = novelQueryService.findNovelsByCategory(requestDto, category);
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
    public SuccessDto<NovelResponse> createNovel(
            @Valid @RequestBody CreateNovelRequest request
    ) {
        NovelResponse newNovel = novelService.createNovel(request);
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
    public SuccessDto<NovelResponse> updateNovel(
            @PathVariable long novelId,
            @Valid @RequestBody UpdateNovelRequest request
    ) {
        NovelResponse updatedNovel = novelService.updateNovel(1, novelId, request);
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
    public SuccessDto<NovelLikeCountResponse> addLikeCount(
            @PathVariable long novelId
    ) {
        NovelLikeCountResponse result = novelService.addLikeCount(1, novelId);
        return new SuccessDto<>(result);
    }

    @DeleteMapping("/{novelId}/likes")
    public SuccessDto<NovelLikeCountResponse> removeLikeCount(
            @PathVariable long novelId
    ) {
        NovelLikeCountResponse result = novelService.removeLikeCount(1, novelId);
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
