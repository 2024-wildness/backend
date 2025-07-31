package com.madiest.moapin.content;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Minimal controller for marking content as reviewed.
 */
@RestController
@RequestMapping("/api/content")
public class ContentController {
    private final ContentRepository repository;

    public ContentController(ContentRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/{id}/reviewed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markReviewed(@PathVariable Long id, Authentication auth) {
        repository.markReviewed(id);
    }
}
