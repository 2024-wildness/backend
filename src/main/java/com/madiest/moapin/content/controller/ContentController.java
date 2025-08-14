package com.madiest.moapin.content.controller;

import com.madiest.moapin.content.dto.ContentCreateRequest;
import com.madiest.moapin.content.dto.ContentResponse;
import com.madiest.moapin.content.dto.ContentUpdateRequest;
import com.madiest.moapin.content.model.Content;
import com.madiest.moapin.content.service.ContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

  private final ContentService contentService;

  @PostMapping
  public ResponseEntity<ContentResponse> createContent(@RequestBody ContentCreateRequest request) {
    Content content =
        contentService.createContent(
            request.getCategoryId(),
            request.getType(),
            request.getTitle(),
            request.getBody(),
            request.getUrl(),
            request.getFileKey());
    return ResponseEntity.ok(ContentResponse.fromEntity(content));
  }

  @PutMapping("/{contentId}")
  public ResponseEntity<ContentResponse> updateContent(
      @PathVariable Long contentId, @RequestBody ContentUpdateRequest request) {
    Content content =
        contentService.updateContent(
            contentId, request.getTitle(), request.getBody(), request.getUrl());
    return ResponseEntity.ok(ContentResponse.fromEntity(content));
  }

  @DeleteMapping("/{contentId}")
  public ResponseEntity<Void> deleteContent(@PathVariable Long contentId) {
    contentService.deleteContent(contentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{contentId}")
  public ResponseEntity<ContentResponse> getContent(@PathVariable Long contentId) {
    Content content = contentService.getContent(contentId);
    return ResponseEntity.ok(ContentResponse.fromEntity(content));
  }

  @GetMapping
  public ResponseEntity<List<ContentResponse>> getContents(@RequestParam Long categoryId) {
    List<Content> contents = contentService.getContents(categoryId);
    List<ContentResponse> response = contents.stream().map(ContentResponse::fromEntity).toList();
    return ResponseEntity.ok(response);
  }
}
