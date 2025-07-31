package com.madiest.moapin.share;

import com.madiest.moapin.content.Content;
import com.madiest.moapin.content.ContentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

/**
 * Service for creating and accessing share links.
 */
@Service
public class ShareLinkService {

    private final ShareLinkRepository repository;
    private final ContentRepository contentRepository;

    public ShareLinkService(ShareLinkRepository repository,
                            ContentRepository contentRepository) {
        this.repository = repository;
        this.contentRepository = contentRepository;
    }

    @Transactional
    public ShareLink createShareLink(Long contentId, Instant expiresAt,
                                     Integer maxDownloads, Authentication auth) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // verify ownership via category service
        if (content.getCategory() != null && (content.getCategory().getUser() == null || 
                !content.getCategory().getUser().getUsername().equals(auth.getName()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        ShareLink link = new ShareLink();
        link.setContent(content);
        link.setToken(UUID.randomUUID().toString());
        link.setExpiresAt(expiresAt);
        link.setMaxDownloads(maxDownloads);
        return repository.save(link);
    }

    @Transactional
    public Content accessShareLink(String token) {
        ShareLink link = repository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (link.isRevoked() || (link.getExpiresAt() != null && link.getExpiresAt().isBefore(Instant.now()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Link expired");
        }
        if (link.getMaxDownloads() != null && link.getDownloadCount() >= link.getMaxDownloads()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Download limit reached");
        }
        link.setDownloadCount(link.getDownloadCount() + 1);
        repository.save(link);
        return link.getContent();
    }

    @Transactional
    public List<ShareLink> listShareLinks(Long contentId, Authentication auth) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (content.getCategory() != null) {
            if (content.getCategory().getUser() == null || !content.getCategory().getUser().getUsername().equals(auth.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        return repository.findByContent(content);
    }

    @Transactional
    public void revokeLink(Long id, Authentication auth) {
        ShareLink link = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (link.getContent().getCategory() != null) {
            if (link.getContent().getCategory().getUser() == null || 
                !link.getContent().getCategory().getUser().getUsername().equals(auth.getName())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        link.setRevoked(true);
        repository.save(link);
    }
}
