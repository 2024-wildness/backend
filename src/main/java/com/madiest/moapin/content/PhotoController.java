package com.madiest.moapin.content;

import com.madiest.moapin.content.payload.PhotoCompleteRequest;
import com.madiest.moapin.content.payload.PhotoDownloadUrlResponse;
import com.madiest.moapin.content.payload.PhotoUploadUrlRequest;
import com.madiest.moapin.content.payload.PhotoUploadUrlResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;

import com.madiest.moapin.config.AppProperties;
import com.madiest.moapin.content.payload.CreateContentRequest;
import com.madiest.moapin.content.payload.ContentResponse;
import com.madiest.moapin.content.ContentType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.madiest.moapin.content.Photo;
import com.madiest.moapin.content.Content;
import com.madiest.moapin.content.ContentService;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * REST endpoints for photo upload/download presigned URLs.
 */
@RestController
@RequestMapping("/api/content/photos")
public class PhotoController {

    private final S3Presigner presigner;
    private final ContentService contentService;
    private final AppProperties props;

    public PhotoController(S3Presigner presigner,
                           ContentService contentService,
                           AppProperties props) {
        this.presigner = presigner;
        this.contentService = contentService;
        this.props = props;
    }

    @PostMapping("/upload-url")
    public ResponseEntity<PhotoUploadUrlResponse> getUploadUrl(
            @Valid @RequestBody PhotoUploadUrlRequest request) {
        String key = "photos/" + System.currentTimeMillis() + "-" + request.getFileName();
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(props.getStorage().getBucket())
                .key(key)
                .contentType(request.getContentType())
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(objectRequest)
                .signatureDuration(Duration.ofMinutes(15))
                .build();
        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
        return ResponseEntity.ok(
                new PhotoUploadUrlResponse(presignedRequest.url().toString(), key));
    }

    @PostMapping("/complete")
    public ResponseEntity<ContentResponse> completeUpload(
            @Valid @RequestBody PhotoCompleteRequest request,
            Authentication auth) {
        CreateContentRequest createReq = new CreateContentRequest();
        createReq.setType(ContentType.PHOTO);
        createReq.setFileKey(request.getKey());
        createReq.setCategoryId(request.getCategoryId());
        Content content = contentService.createContent(createReq, auth);
        return ResponseEntity.ok(ContentResponse.fromEntity(content));
    }

    @GetMapping("/{id}/download-url")
    public ResponseEntity<PhotoDownloadUrlResponse> getDownloadUrl(
            @PathVariable Long id,
            Authentication auth) {
        Content content = contentService.getContent(id, auth);
        if (!(content instanceof Photo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Content is not a photo");
        }
        String key = ((Photo) content).getFileKey();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(props.getStorage().getBucket())
                .key(key)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(15))
                .build();
        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
        return ResponseEntity.ok(new PhotoDownloadUrlResponse(presignedRequest.url().toString()));
    }
}