package com.madiest.moapin.content.controller;

import com.madiest.moapin.content.dto.ContentResponse;
import com.madiest.moapin.content.dto.PhotoCompleteRequest;
import com.madiest.moapin.content.dto.PhotoDownloadUrlResponse;
import com.madiest.moapin.content.dto.PhotoUploadUrlRequest;
import com.madiest.moapin.content.dto.PhotoUploadUrlResponse;
import com.madiest.moapin.content.model.Content;
import com.madiest.moapin.content.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

/**
 * REST endpoints for photo upload/download presigned URLs.
 */
@RestController
@RequestMapping("/api/content/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final ContentService contentService;

    @PostMapping("/upload-url")
    public ResponseEntity<PhotoUploadUrlResponse> getUploadUrl(
            @Valid @RequestBody PhotoUploadUrlRequest request) {
        ContentService.PhotoUploadInfo uploadInfo = contentService.createPhotoUploadUrl(request.getFileName(), request.getContentType());
        return ResponseEntity.ok(
                new PhotoUploadUrlResponse(uploadInfo.uploadUrl().toString(), uploadInfo.fileKey()));
    }

    @PostMapping("/complete")
    public ResponseEntity<ContentResponse> completeUpload(
            @Valid @RequestBody PhotoCompleteRequest request) {
        Content content = contentService.completePhotoUpload(request.getCategoryId(), request.getTitle(), request.getKey());
        return ResponseEntity.ok(ContentResponse.fromEntity(content));
    }

    @GetMapping("/{id}/download-url")
    public ResponseEntity<PhotoDownloadUrlResponse> getDownloadUrl(
            @PathVariable Long id) {
        URL downloadUrl = contentService.createPhotoDownloadUrl(id);
        return ResponseEntity.ok(new PhotoDownloadUrlResponse(downloadUrl.toString()));
    }
}
