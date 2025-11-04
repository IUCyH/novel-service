package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.common.dto.response.PagingResponse;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.web.dto.response.NovelResponse;
import com.iucyh.novelservice.novel.web.dto.request.NovelPagingRequest;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.repository.query.dto.NovelPagingQueryDto;
import com.iucyh.novelservice.novel.service.codec.NovelCursorBase64Codec;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.novel.repository.query.NovelQueryRepository;
import com.iucyh.novelservice.novel.repository.query.condition.NovelSearchCondition;
import com.iucyh.novelservice.novel.repository.query.cursor.NovelCursor;
import com.iucyh.novelservice.novel.repository.query.pagingquery.NovelPagingQuery;
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
    public List<NovelResponse> findNovelsByCategoryInSummary(NovelCategory category) {
        NovelSearchCondition searchCondition = new NovelSearchCondition(null, 10);
        NovelPagingQuery pagingQuery = getPagingQuery(NovelSortType.POPULAR);
        List<? extends NovelPagingQueryDto> novels = novelQueryRepository.findNovelsByCategory(searchCondition, pagingQuery, category);

        return mapToNovelResponseList(novels);
    }

    /**
     * 이번 달 신작 소설을 업데이트순 상위 30개만 조회하는 메서드
     */
    public List<NovelResponse> findNewNovelsInSummary() {
        NovelSearchCondition searchCondition = new NovelSearchCondition(null, 30);
        NovelPagingQuery pagingQuery = getPagingQuery(NovelSortType.LAST_UPDATE);
        List<? extends NovelPagingQueryDto> novels = novelQueryRepository.findNewNovels(searchCondition, pagingQuery);

        return mapToNovelResponseList(novels);
    }

    public PagingResponse<NovelResponse> findNovels(NovelPagingRequest pagingRequest) {
        return executePagingQuery(pagingRequest,
                (searchCondition, pagingQuery) ->
                        novelQueryRepository.findNovels(searchCondition, pagingQuery)
        );
    }

    public PagingResponse<NovelResponse> findNovelsByCategory(NovelPagingRequest pagingRequest, NovelCategory category) {
        return executePagingQuery(pagingRequest,
                (searchCondition, pagingQuery) ->
                        novelQueryRepository.findNovelsByCategory(searchCondition, pagingQuery, category)
        );
    }

    public PagingResponse<NovelResponse> findNewNovels(NovelPagingRequest pagingRequest) {
        return executePagingQuery(pagingRequest,
                (searchCondition, pagingQuery) ->
                        novelQueryRepository.findNewNovels(searchCondition, pagingQuery)
        );
    }

    private PagingResponse<NovelResponse> executePagingQuery(
            NovelPagingRequest pagingRequest,
            BiFunction<NovelSearchCondition, NovelPagingQuery, List<? extends NovelPagingQueryDto>> queryFunc
    ) {
        NovelSortType sortType = NovelSortType.of(pagingRequest.sort());
        String cursor = pagingRequest.cursor();
        Integer limit = pagingRequest.limit();

        NovelPagingQuery pagingQuery = getPagingQuery(sortType);
        NovelSearchCondition searchCondition = createSearchCondition(sortType, cursor, limit);

        List<? extends NovelPagingQueryDto> result = queryFunc.apply(searchCondition, pagingQuery);
        // TODO: 각 조회 조건별 쿼리 세분화 필요, 쿼리 호출 최소화 필요
        long totalCount = novelRepository.countByDeletedAtIsNull();

        if (result.isEmpty()) {
            return NovelResponseMapper.toPagingResultDto(List.of(), totalCount, null);
        }

        List<NovelResponse> novelResponses = mapToNovelResponseList(result);
        String newCursor = createNewEncodedCursor(pagingQuery, result);
        return NovelResponseMapper.toPagingResultDto(novelResponses, totalCount, newCursor);
    }

    private NovelPagingQuery getPagingQuery(NovelSortType sortType) {
        NovelPagingQuery pagingQuery = pagingQueryMap.get(sortType);
        if (pagingQuery == null) {
            throw new IllegalArgumentException("There's no matched paging query with: " + sortType.name());
        }
        return pagingQuery;
    }

    private NovelSearchCondition createSearchCondition(NovelSortType sortType, String encodedCursor, int limit) {
        NovelCursor decodedCursor = base64Codec.decode(encodedCursor, sortType.getSupportedCursorClass());
        return new NovelSearchCondition(decodedCursor, limit);
    }

    private List<NovelResponse> mapToNovelResponseList(List<? extends NovelPagingQueryDto> novels) {
        return novels.stream()
                .map(n -> NovelResponseMapper.toNovelResponse(n.getNovel()))
                .toList();
    }

    private String createNewEncodedCursor(NovelPagingQuery pagingQuery, List<? extends NovelPagingQueryDto> novels) {
        NovelPagingQueryDto lastResult = novels.get(novels.size() - 1);
        NovelCursor newCursor = pagingQuery.createCursor(lastResult);
        return base64Codec.encode(newCursor);
    }
}
