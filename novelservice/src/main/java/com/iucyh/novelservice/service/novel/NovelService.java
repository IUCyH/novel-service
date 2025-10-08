package com.iucyh.novelservice.service.novel;

import com.iucyh.novelservice.common.exception.novel.DuplicateNovelTitle;
import com.iucyh.novelservice.common.exception.novel.NovelNotFound;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.IdDto;
import com.iucyh.novelservice.dto.novel.CreateNovelDto;
import com.iucyh.novelservice.dto.novel.NovelDtoMapper;
import com.iucyh.novelservice.dto.novel.UpdateNovelDto;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;

    public IdDto createNovel(CreateNovelDto createNovelDto) {
        String title = createNovelDto.getTitle();
        boolean isDuplicateTitle = novelRepository.existsByTitle(title);

        if (isDuplicateTitle) {
            throw new DuplicateNovelTitle(title);
        }

        Novel newNovel = NovelDtoMapper.toNovelEntity(createNovelDto);
        novelRepository.save(newNovel);

        String newPublicId = newNovel.getPublicId().toString();
        return new IdDto(newPublicId);
    }

    public void updateNovel(long userId, UUID publicId, UpdateNovelDto novelDto) {
        Novel novel = findNovelWithUserId(userId, publicId);
        NovelCategory newCategory = NovelCategory.of(novelDto.getCategory());

        novel.updateTextMetaData(novelDto.getTitle(), novelDto.getDescription());
        novel.updateCategory(newCategory);
    }

    public void deleteNovel(long userId, UUID publicId) {
        Novel novel = findNovelWithUserId(userId, publicId);
        novel.softDelete();
    }

    private Novel findNovelWithUserId(long userId, UUID publicId) {
        return novelRepository.findByPublicId(publicId)
                .orElseThrow(() -> new NovelNotFound(publicId.toString()));
    }
}
