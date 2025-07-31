package com.madiest.moapin.content;

import com.madiest.moapin.content.payload.CreateContentRequest;
import com.madiest.moapin.content.Content;
import com.madiest.moapin.content.payload.ContentResponse;
import com.madiest.moapin.content.payload.UpdateContentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

import javax.validation.Valid;

/**
 * REST endpoints for content CRUD operations.
 */
@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * List all content items for the authenticated user,
     * pinned items first.
     */
    @GetMapping
    public ResponseEntity<java.util.List<ContentResponse>> listContent(
            Authentication auth) {
        java.util.List<Content> items = contentService.listContent(auth);
        java.util.List<ContentResponse> resp = items.stream()
                .map(ContentResponse::fromEntity)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<ContentResponse> createContent(
            @Valid @RequestBody CreateContentRequest request,
            Authentication auth) {
        Content content = contentService.createContent(request, auth);
        return ResponseEntity.ok(ContentResponse.fromEntity(content));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> getContent(
            @PathVariable Long id,
            Authentication auth) {
        Content content = contentService.getContent(id, auth);
        return ResponseEntity.ok(ContentResponse.fromEntity(content));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateContentRequest request,
            Authentication auth) {
        Content content = contentService.updateContent(id, request, auth);
        return ResponseEntity.ok(ContentResponse.fromEntity(content));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(
            @PathVariable Long id,
            Authentication auth) {
        contentService.deleteContent(id, auth);
        return ResponseEntity.noContent().build();
    }

    /**
     * Toggle the pinned status of a content item.
     */
    @PatchMapping("/{id}/pin")
    public ResponseEntity<Void> togglePin(
            @PathVariable Long id,
            Authentication auth) {
        contentService.togglePin(id, auth);
        return ResponseEntity.noContent().build();
    }
}