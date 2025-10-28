package com.iucyh.novelservice.service.novel;

import com.iucyh.novelservice.common.exception.novel.DuplicateNovelTitle;
import com.iucyh.novelservice.common.exception.novel.NovelNotFound;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.novel.*;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;

    public NovelDto createNovel(CreateNovelDto createNovelDto) {
        String title = createNovelDto.getTitle();
        boolean isDuplicateTitle = novelRepository.existsByTitle(title);

        if (isDuplicateTitle) {
            throw new DuplicateNovelTitle(title);
        }

        Novel newNovel = NovelDtoMapper.toNovelEntity(createNovelDto);
        novelRepository.save(newNovel);

        return NovelDtoMapper.toNovelDto(newNovel);
    }

    public NovelDto updateNovel(long userId, long novelId, UpdateNovelDto novelDto) {
        Novel novel = findNovelWithUserId(userId, novelId);
        NovelCategory newCategory = NovelCategory.of(novelDto.getCategory());

        novel.updateTextMetaData(novelDto.getTitle(), novelDto.getDescription());
        novel.updateCategory(newCategory);

        return NovelDtoMapper.toNovelDto(novel);
    }

    public UpdateLikeCountDto addLikeCount(long userId, long novelId) {
        Novel novel = findNovelWithUserId(userId, novelId);
        novel.addLikes(1);

        return NovelDtoMapper.toNovelLikeCountDto(novel.getLikeCount());
    }

    public UpdateLikeCountDto removeLikeCount(long userId, long novelId) {
        Novel novel = findNovelWithUserId(userId, novelId);
        novel.removeLikes(1);

        return NovelDtoMapper.toNovelLikeCountDto(novel.getLikeCount());
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
