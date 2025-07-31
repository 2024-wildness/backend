package com.madiest.moapin.share;

import com.madiest.moapin.content.Content;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

/**
 * REST controller for share link operations.
 */
@RestController
@RequestMapping("/api/share")
public class ShareLinkController {

    private final ShareLinkService service;

    public ShareLinkController(ShareLinkService service) {
        this.service = service;
    }

    public static class CreateRequest {
        @NotNull
        private Long contentId;
        private Instant expiresAt;
        private Integer maxDownloads;
        public Long getContentId() { return contentId; }
        public void setContentId(Long contentId) { this.contentId = contentId; }
        public Instant getExpiresAt() { return expiresAt; }
        public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
        public Integer getMaxDownloads() { return maxDownloads; }
        public void setMaxDownloads(Integer maxDownloads) { this.maxDownloads = maxDownloads; }
    }

    @PostMapping
    public ShareLink create(@Valid @RequestBody CreateRequest req, Authentication auth) {
        return service.createShareLink(req.getContentId(), req.getExpiresAt(), req.getMaxDownloads(), auth);
    }

    @GetMapping("/{token}")
    public Content access(@PathVariable String token) {
        return service.accessShareLink(token);
    }

    @GetMapping
    public List<ShareLink> list(@RequestParam @NotNull Long contentId, Authentication auth) {
        return service.listShareLinks(contentId, auth);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Long id, Authentication auth) {
        service.revokeLink(id, auth);
        return ResponseEntity.ok().build();
    }
}
