package com.madiest.moapin.search;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.SearchResult;
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
    public SearchResult search(@RequestParam("q") String query, Authentication auth) throws Exception {
        Index index = client.index("contents");
        SearchRequest request = new SearchRequest(query);
        request.setFilter("user = '" + auth.getName() + "'");
        return index.search(request);
    }
}
