package com.madiest.moapin.content;

import com.madiest.moapin.category.Category;
import com.madiest.moapin.category.CategoryService;
import com.madiest.moapin.content.payload.CreateContentRequest;
import com.madiest.moapin.content.payload.UpdateContentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service for managing content items.
 */
@Service
public class ContentService {

    private final ContentRepository contentRepository;
    private final CategoryService categoryService;

    public ContentService(ContentRepository contentRepository,
                          CategoryService categoryService) {
        this.contentRepository = contentRepository;
        this.categoryService = categoryService;
    }

    /**
     * Create and persist a new content item based on the request.
     */
    @Transactional
    public Content createContent(CreateContentRequest req, Authentication auth) {
        Category category = null;
        if (req.getCategoryId() != null) {
            category = categoryService.getCategory(req.getCategoryId(), auth);
        }
        Content content;
        switch (req.getType()) {
            case PHOTO:
                if (req.getFileKey() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "fileKey is required for PHOTO content");
                }
                Photo photo = new Photo();
                photo.setFileKey(req.getFileKey());
                photo.setCategory(category);
                content = contentRepository.save(photo);
                break;
            case LINK:
                if (req.getUrl() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "url is required for LINK content");
                }
                Link link = new Link();
                link.setUrl(req.getUrl());
                link.setCategory(category);
                content = contentRepository.save(link);
                break;
            case NOTE:
                if (req.getTextContent() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "textContent is required for NOTE content");
                }
                Note note = new Note();
                note.setTextContent(req.getTextContent());
                note.setCategory(category);
                content = contentRepository.save(note);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unsupported content type: " + req.getType());
        }
        return content;
    }

    /**
     * Retrieve a content item if it belongs to the authenticated user.
     */
    /**
     * Retrieve a content item if it belongs to the authenticated user and increment its view count.
     */
    @Transactional
    public Content getContent(Long id, Authentication auth) {
        Content content = loadAuthorizedContent(id, auth);
        content.setViewCount(content.getViewCount() + 1);
        return contentRepository.save(content);
    }

    /**
     * Update fields of an existing content item without affecting view count.
     */
    @Transactional
    public Content updateContent(Long id, UpdateContentRequest req, Authentication auth) {
        Content content = loadAuthorizedContent(id, auth);
        if (req.getCategoryId() != null) {
            Category category = categoryService.getCategory(req.getCategoryId(), auth);
            content.setCategory(category);
        } else {
            content.setCategory(null);
        }
        if (content instanceof Photo && req.getFileKey() != null) {
            ((Photo) content).setFileKey(req.getFileKey());
        } else if (content instanceof Link && req.getUrl() != null) {
            ((Link) content).setUrl(req.getUrl());
        } else if (content instanceof Note && req.getTextContent() != null) {
            ((Note) content).setTextContent(req.getTextContent());
        }
        return contentRepository.save(content);
    }

    /**
     * Delete a content item belonging to the authenticated user.
     */
    @Transactional
    public void deleteContent(Long id, Authentication auth) {
        Content content = loadAuthorizedContent(id, auth);
        contentRepository.delete(content);
    }

    /**
     * List all content items for the authenticated user,
     * with pinned items prioritized at the top.
     */
    @Transactional(readOnly = true)
    public java.util.List<Content> listContent(Authentication auth) {
        return contentRepository.findByCategoryUserUsernameOrderByPinnedDescCreatedAtDesc(
                auth.getName());
    }

    /**
     * Toggle the pinned status of a content item for the authenticated user.
     */
    @Transactional
    public void togglePin(Long id, Authentication auth) {
        Content content = loadAuthorizedContent(id, auth);
        content.setPinned(!content.isPinned());
        contentRepository.save(content);
    }

    /**
     * Load and authorize a content item without modifying it.
     */
    private Content loadAuthorizedContent(Long id, Authentication auth) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Content not found"));
        if (content.getCategory() != null) {
            categoryService.getCategory(content.getCategory().getId(), auth);
        }
        return content;
    }
}