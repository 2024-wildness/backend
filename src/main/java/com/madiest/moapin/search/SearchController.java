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

    public SearchController(Client client) {
        this.client = client;
    }

    @GetMapping
    public SearchResult search(@RequestParam("q") SearchRequest request, Authentication auth) throws Exception {
        Index index = client.index("contents");

        // 1. setFilter는 단일 문자열이 아닌, 문자열 배열(String[])을 인자로 받습니다.
        request.setFilter(new String[]{"user = '" + auth.getName() + "'"});

        // 2. index.search()가 반환하는 원본 SearchResult 타입을 그대로 반환합니다.
        return index.search(request.getQ());
    }
}