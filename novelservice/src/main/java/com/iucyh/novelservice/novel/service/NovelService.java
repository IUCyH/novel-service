package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.novel.exception.DuplicateNovelTitle;
import com.iucyh.novelservice.novel.exception.NovelNotFound;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.dto.mapper.NovelRequestMapper;
import com.iucyh.novelservice.novel.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.dto.request.CreateNovelRequest;
import com.iucyh.novelservice.novel.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.dto.request.UpdateNovelRequest;
import com.iucyh.novelservice.novel.dto.response.NovelResponse;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;

    public NovelResponse createNovel(CreateNovelRequest request) {
        String title = request.title();
        boolean isDuplicateTitle = novelRepository.existsByTitle(title);

        if (isDuplicateTitle) {
            throw new DuplicateNovelTitle(title);
        }

        Novel newNovel = NovelRequestMapper.toNovel(request);
        novelRepository.save(newNovel);

        return NovelResponseMapper.toNovelResponse(newNovel);
    }

    public NovelResponse updateNovel(long userId, long novelId, UpdateNovelRequest request) {
        Novel novel = findNovelWithUserId(userId, novelId);
        NovelCategory newCategory = NovelCategory.of(request.category());

        novel.updateTextMetaData(request.title(), request.description());
        novel.updateCategory(newCategory);

        return NovelResponseMapper.toNovelResponse(novel);
    }

    public NovelLikeCountResponse addLikeCount(long userId, long novelId) {
        Novel novel = findNovelWithUserId(userId, novelId);
        novel.addLikes(1);

        return NovelResponseMapper.toNovelLikeCountResponse(novel.getLikeCount());
    }

    public NovelLikeCountResponse removeLikeCount(long userId, long novelId) {
        Novel novel = findNovelWithUserId(userId, novelId);
        novel.removeLikes(1);

        return NovelResponseMapper.toNovelLikeCountResponse(novel.getLikeCount());
    }

    public void deleteNovel(long userId, long novelId) {
        Novel novel = findNovelWithUserId(userId, novelId);
        novel.softDelete();
    }

    private Novel findNovelWithUserId(long userId, long novelId) {
        return novelRepository.findById(novelId)
                .orElseThrow(() -> new NovelNotFound(novelId));
    }
}
