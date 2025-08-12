package com.madiest.moapin.share.service;

import com.madiest.moapin.content.model.Content;
import com.madiest.moapin.content.repository.ContentRepository;
import com.madiest.moapin.share.model.ShareLink;
import com.madiest.moapin.share.repository.ShareLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ShareLinkService {

    private final ShareLinkRepository shareLinkRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public ShareLink createShareLink(Long contentId, Instant expiresAt, Integer maxDownloads) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
        ShareLink shareLink = new ShareLink(content, expiresAt, maxDownloads);
        return shareLinkRepository.save(shareLink);
    }

    @Transactional
    public Content accessShareLink(String token) {
        ShareLink shareLink = shareLinkRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Share link not found"));

        if (shareLink.getExpiresAt() != null && shareLink.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.GONE, "Share link has expired");
        }

        if (shareLink.getMaxDownloads() != null && shareLink.getDownloadCount() >= shareLink.getMaxDownloads()) {
            throw new ResponseStatusException(HttpStatus.GONE, "Download limit reached");
        }

        shareLink.incrementDownloadCount();
        shareLinkRepository.save(shareLink);

        return shareLink.getContent();
    }

    @Transactional
    public void deleteShareLink(String shareId) {
        shareLinkRepository.deleteById(shareId);
    }

    @Transactional(readOnly = true)
    public void getShareLinks() {
        // TODO: Implement get share links logic
    }
}
