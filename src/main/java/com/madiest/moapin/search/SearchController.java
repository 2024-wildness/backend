package com.madiest.moapin.search;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for searching content via MeiliSearch.
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final Client client;

    /**
     * SearchController의 인스턴스를 생성합니다.
     *
     * @param client MeiliSearch와의 통신에 사용되는 클라이언트 인스턴스
     */
    public SearchController(Client client) {
        this.client = client;
    }

    /**
     * 인증된 사용자의 콘텐츠만을 대상으로 MeiliSearch에서 검색을 수행합니다.
     *
     * @param request 검색 요청 정보를 포함하는 SearchRequest 객체
     * @param auth 현재 인증된 사용자의 인증 정보
     * @return 검색 결과를 담은 SearchResult 객체
     * @throws Exception MeiliSearch 검색 중 오류가 발생한 경우
     */
    @GetMapping
    public SearchResult search(@RequestParam("q") SearchRequest request, Authentication auth) throws Exception {
        Index index = client.index("contents");

        // 1. setFilter는 단일 문자열이 아닌, 문자열 배열(String[])을 인자로 받습니다.
        request.setFilter(new String[]{"user = '" + auth.getName() + "'"});

        // 2. index.search()가 반환하는 원본 SearchResult 타입을 그대로 반환합니다.
        return index.search(request.getQ());
    }
}