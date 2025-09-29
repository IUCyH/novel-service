package com.iucyh.novelservice.service.novel;

import com.iucyh.novelservice.domain.novel.NovelCategory;
import com.iucyh.novelservice.dto.PagingResultDto;
import com.iucyh.novelservice.dto.novel.NovelDto;
import com.iucyh.novelservice.dto.novel.NovelDtoMapper;
import com.iucyh.novelservice.dto.novel.NovelPagingRequestDto;
import com.iucyh.novelservice.dto.novel.NovelSortType;
import com.iucyh.novelservice.dto.novel.query.NovelPagingQueryDto;
import com.iucyh.novelservice.repository.novel.NovelRepository;
import com.iucyh.novelservice.repository.novel.query.NovelQueryRepository;
import com.iucyh.novelservice.repository.novel.query.NovelSearchCondition;
import com.iucyh.novelservice.repository.novel.query.cursor.NovelCursor;
import com.iucyh.novelservice.repository.novel.query.pagingquery.NovelPagingQuery;
import com.iucyh.novelservice.service.novel.codec.NovelCursorBase64Codec;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class NovelQueryService {

    private final NovelCursorBase64Codec base64Codec;
    private final NovelRepository novelRepository;
    private final NovelQueryRepository novelQueryRepository;
    private final Map<NovelSortType, NovelPagingQuery> pagingQueryMap;

    public NovelQueryService(
            NovelCursorBase64Codec base64Codec,
            NovelRepository novelRepository,
            NovelQueryRepository novelQueryRepository,
            List<NovelPagingQuery> pagingQueries
    ) {
        this.base64Codec = base64Codec;
        this.novelRepository = novelRepository;
        this.novelQueryRepository = novelQueryRepository;
        this.pagingQueryMap = pagingQueries
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                NovelPagingQuery::getSupportedSortType,
                                Function.identity()
                        )
                );
    }

    /**
     * 카테고리 별 소설을 인기순 상위 10개만 조회하는 메서드
     */
    public List<NovelDto> findNovelsByCategoryInSummary(NovelCategory category) {
        NovelSearchCondition searchCondition = new NovelSearchCondition(null);
        NovelPagingQuery pagingQuery = getPagingQuery(NovelSortType.POPULAR);
        List<? extends NovelPagingQueryDto> novels = novelQueryRepository.findNovelsByCategory(searchCondition, pagingQuery, category, 10);

        return mapToNovelDtoList(novels);
    }

    /**
     * 이번 달 신작 소설을 업데이트순 상위 30개만 조회하는 메서드
     */
    public List<NovelDto> findNewNovelsInSummary() {
        NovelSearchCondition searchCondition = new NovelSearchCondition(null);
        NovelPagingQuery pagingQuery = getPagingQuery(NovelSortType.LAST_UPDATE);
        List<? extends NovelPagingQueryDto> novels = novelQueryRepository.findNewNovels(searchCondition, pagingQuery, 30);

        return mapToNovelDtoList(novels);
    }

    public PagingResultDto<NovelDto> findNovels(NovelPagingRequestDto pagingRequest) {
        Integer limit = pagingRequest.getLimit();
        return executePagingQuery(pagingRequest,
                (searchCondition, pagingQuery) ->
                        novelQueryRepository.findNovels(searchCondition, pagingQuery, limit)
        );
    }

    public PagingResultDto<NovelDto> findNovelsByCategory(NovelPagingRequestDto pagingRequest, NovelCategory category) {
        Integer limit = pagingRequest.getLimit();
        return executePagingQuery(pagingRequest,
                (searchCondition, pagingQuery) ->
                        novelQueryRepository.findNovelsByCategory(searchCondition, pagingQuery, category, limit)
        );
    }

    public PagingResultDto<NovelDto> findNewNovels(NovelPagingRequestDto pagingRequest) {
        Integer limit = pagingRequest.getLimit();
        return executePagingQuery(pagingRequest,
                (searchCondition, pagingQuery) ->
                        novelQueryRepository.findNewNovels(searchCondition, pagingQuery, limit)
        );
    }

    private PagingResultDto<NovelDto> executePagingQuery(
            NovelPagingRequestDto pagingRequest,
            BiFunction<NovelSearchCondition, NovelPagingQuery, List<? extends NovelPagingQueryDto>> queryFunc
    ) {
        NovelSortType sortType = pagingRequest.getSort();
        String cursor = pagingRequest.getCursor();

        NovelPagingQuery pagingQuery = getPagingQuery(sortType);
        NovelSearchCondition searchCondition = createSearchCondition(sortType, cursor);

        List<? extends NovelPagingQueryDto> result = queryFunc.apply(searchCondition, pagingQuery);
        // TODO: 각 조회 조건별 쿼리 세분화 필요, 쿼리 호출 최소화 필요
        long totalCount = novelRepository.countByDeletedAtIsNull();

        if (result.isEmpty()) {
            return NovelDtoMapper.toPagingResultDto(List.of(), totalCount, null);
        }

        List<NovelDto> novelDtoList = mapToNovelDtoList(result);
        String newCursor = createNewEncodedCursor(pagingQuery, result);
        return NovelDtoMapper.toPagingResultDto(novelDtoList, totalCount, newCursor);
    }

    private NovelPagingQuery getPagingQuery(NovelSortType sortType) {
        NovelPagingQuery pagingQuery = pagingQueryMap.get(sortType);
        if (pagingQuery == null) {
            throw new IllegalArgumentException("There's no matched paging query with: " + sortType.name());
        }
        return pagingQuery;
    }

    private NovelSearchCondition createSearchCondition(NovelSortType sortType, String encodedCursor) {
        NovelCursor decodedCursor = base64Codec.decode(encodedCursor, sortType.getSupportedCursorClass());
        return new NovelSearchCondition(decodedCursor);
    }

    private List<NovelDto> mapToNovelDtoList(List<? extends NovelPagingQueryDto> novels) {
        return novels.stream()
                .map(n -> NovelDtoMapper.toNovelDto(n.getNovel()))
                .toList();
    }

    private String createNewEncodedCursor(NovelPagingQuery pagingQuery, List<? extends NovelPagingQueryDto> novels) {
        NovelPagingQueryDto lastResult = novels.get(novels.size() - 1);
        NovelCursor newCursor = pagingQuery.createCursor(lastResult);
        return base64Codec.encode(newCursor);
    }
}
