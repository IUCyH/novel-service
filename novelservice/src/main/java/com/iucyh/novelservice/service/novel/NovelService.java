package com.iucyh.novelservice.service.novel;

import com.iucyh.novelservice.common.exception.novel.DuplicateNovelTitle;
import com.iucyh.novelservice.domain.novel.Novel;
import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.IdDto;
import com.iucyh.novelservice.dto.novel.CreateNovelDto;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;

    public IdDto createNovel(long userId, CreateNovelDto createNovelDto) {
        String title = createNovelDto.getTitle();
        String description = createNovelDto.getDescription();
        NovelCategory category = createNovelDto.getCategory();

        boolean isDuplicateTitle = novelRepository.existsByTitle(title);
        if (isDuplicateTitle) {
            throw new DuplicateNovelTitle(title);
        }

        Novel newNovel = Novel.of(title, description, category);
        novelRepository.save(newNovel);
        return new IdDto(newNovel.getPublicId());
    }
}
