package com.madiest.moapin.share;

import com.madiest.moapin.content.Content;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;


/**
 * REST controller for share link operations.
 */
@RestController
@RequestMapping("/api/share")
public class ShareLinkController {

    private final ShareLinkService service;

    /**
     * ShareLinkController의 인스턴스를 생성하고, 공유 링크 관련 서비스를 주입합니다.
     *
     * @param service 공유 링크 관련 비즈니스 로직을 처리하는 서비스
     */
    public ShareLinkController(ShareLinkService service) {
        this.service = service;
    }

    @Setter
    @Getter
    public static class CreateRequest {
        @NotNull
        private Long contentId;
        private Instant expiresAt;
        private Integer maxDownloads;

    }

    /**
     * 새로운 공유 링크를 생성하여 반환합니다.
     *
     * @param req 공유 링크 생성에 필요한 요청 데이터
     * @param auth 인증 정보
     * @return 생성된 공유 링크 객체
     */
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
