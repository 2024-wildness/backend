package com.madiest.moapin.content.service;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.category.model.Category;
import com.madiest.moapin.category.repository.CategoryRepository;
import com.madiest.moapin.common.config.AppProperties;
import com.madiest.moapin.content.model.*;
import com.madiest.moapin.content.repository.ContentRepository;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class ContentService {

  private final ContentRepository contentRepository;
  private final CategoryRepository categoryRepository;
  private final S3Presigner s3Presigner;
  private final AppProperties appProperties;

  @Transactional
  public Content createContent(
      Long categoryId, String type, String title, String body, String url, String fileKey) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Category category =
        categoryRepository
            .findByIdAndUser(categoryId, user)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    Content content;
    switch (ContentType.valueOf(type)) {
      case PHOTO:
        content = new Photo(title, user, category, fileKey);
        break;
      case LINK:
        content = new Link(title, user, category, url);
        break;
      case NOTE:
        content = new Note(title, user, category, body);
        break;
      default:
        throw new IllegalArgumentException("Unknown content type: " + type);
    }

    return contentRepository.save(content);
  }

  @Transactional
  public Content updateContent(Long contentId, String title, String body, String url) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Content content =
        contentRepository
            .findByIdAndUser(contentId, user)
            .orElseThrow(() -> new RuntimeException("Content not found"));

    content.setTitle(title);

    if (content instanceof Link) {
      ((Link) content).setUrl(url);
    } else if (content instanceof Note) {
      ((Note) content).setBody(body);
    }

    return contentRepository.save(content);
  }

  @Transactional
  public void deleteContent(Long contentId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Content content =
        contentRepository
            .findByIdAndUser(contentId, user)
            .orElseThrow(() -> new RuntimeException("Content not found"));
    // TODO: Delete file from MinIO if it's a Photo
    contentRepository.delete(content);
  }

  @Transactional(readOnly = true)
  public Content getContent(Long contentId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return contentRepository
        .findByIdAndUser(contentId, user)
        .orElseThrow(() -> new RuntimeException("Content not found"));
  }

  @Transactional(readOnly = true)
  public List<Content> getContents(Long categoryId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return contentRepository.findAllByCategory_IdAndUser(categoryId, user);
  }

  public record PhotoUploadInfo(URL uploadUrl, String fileKey) {}

  @Transactional
  public PhotoUploadInfo createPhotoUploadUrl(String fileName, String contentType) {
    String fileKey = "photos/" + UUID.randomUUID() + "-" + fileName;
    PutObjectRequest objectRequest =
        PutObjectRequest.builder()
            .bucket(appProperties.getStorage().getBucket())
            .key(fileKey)
            .contentType(contentType)
            .build();
    PutObjectPresignRequest presignRequest =
        PutObjectPresignRequest.builder()
            .putObjectRequest(objectRequest)
            .signatureDuration(Duration.ofMinutes(15))
            .build();
    URL uploadUrl = s3Presigner.presignPutObject(presignRequest).url();
    return new PhotoUploadInfo(uploadUrl, fileKey);
  }

  @Transactional
  public Content completePhotoUpload(Long categoryId, String title, String fileKey) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Category category =
        categoryRepository
            .findByIdAndUser(categoryId, user)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    // TODO: Verify file exists in MinIO with the given fileKey

    Photo photo = new Photo(title, user, category, fileKey);
    return contentRepository.save(photo);
  }

  @Transactional(readOnly = true)
  public URL createPhotoDownloadUrl(Long contentId) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Content content =
        contentRepository
            .findByIdAndUser(contentId, user)
            .orElseThrow(() -> new RuntimeException("Content not found"));

    if (!(content instanceof Photo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content is not a photo");
    }

    String key = ((Photo) content).getFileKey();
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(appProperties.getStorage().getBucket()).key(key).build();
    GetObjectPresignRequest presignRequest =
        GetObjectPresignRequest.builder()
            .getObjectRequest(getObjectRequest)
            .signatureDuration(Duration.ofMinutes(15))
            .build();
    return s3Presigner.presignGetObject(presignRequest).url();
  }
}
