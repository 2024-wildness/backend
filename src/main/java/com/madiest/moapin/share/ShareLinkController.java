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

    /**
     * ShareLinkController의 인스턴스를 생성한다.
     *
     * @param service 공유 링크 관련 비즈니스 로직을 처리하는 서비스
     */
    public ShareLinkController(ShareLinkService service) {
        this.service = service;
    }

    public static class CreateRequest {
        @NotNull
        private Long contentId;
        private Instant expiresAt;
        private Integer maxDownloads;
        /**
 * 공유 링크를 생성할 때 사용할 콘텐츠의 ID를 반환합니다.
 *
 * @return 콘텐츠 ID
 */
public Long getContentId() { return contentId; }
        /**
 * contentId 값을 설정합니다.
 *
 * @param contentId 공유할 콘텐츠의 ID
 */
public void setContentId(Long contentId) { this.contentId = contentId; }
        /**
 * 공유 링크의 만료 시간을 반환합니다.
 *
 * @return 만료 시간(`Instant`) 또는 설정되지 않은 경우 `null`
 */
public Instant getExpiresAt() { return expiresAt; }
        /**
 * 공유 링크의 만료 시간을 설정합니다.
 *
 * @param expiresAt 만료 시간(UTC)
 */
public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
        /**
 * 최대 다운로드 횟수를 반환합니다.
 *
 * @return 최대 다운로드 횟수 또는 지정되지 않은 경우 null
 */
public Integer getMaxDownloads() { return maxDownloads; }
        /**
 * 공유 링크의 최대 다운로드 횟수를 설정합니다.
 *
 * @param maxDownloads 허용할 최대 다운로드 횟수
 */
public void setMaxDownloads(Integer maxDownloads) { this.maxDownloads = maxDownloads; }
    }

    /**
     * 새로운 공유 링크를 생성하여 반환합니다.
     *
     * @param req 공유 링크 생성에 필요한 요청 정보
     * @return 생성된 공유 링크 객체
     */
    @PostMapping
    public ShareLink create(@Valid @RequestBody CreateRequest req, Authentication auth) {
        return service.createShareLink(req.getContentId(), req.getExpiresAt(), req.getMaxDownloads(), auth);
    }

    /**
     * 주어진 토큰을 사용하여 공유 링크에 접근하고 해당 콘텐츠를 반환합니다.
     *
     * @param token 공유 링크 토큰
     * @return 공유된 콘텐츠 객체
     */
    @GetMapping("/{token}")
    public Content access(@PathVariable String token) {
        return service.accessShareLink(token);
    }

    /**
     * 지정된 콘텐츠 ID에 대한 모든 공유 링크 목록을 반환합니다.
     *
     * @param contentId 공유 링크를 조회할 콘텐츠의 ID
     * @return 해당 콘텐츠에 대한 공유 링크 목록
     */
    @GetMapping
    public List<ShareLink> list(@RequestParam Long contentId, Authentication auth) {
        return service.listShareLinks(contentId, auth);
    }

    /**
     * 지정된 공유 링크를 취소(무효화)합니다.
     *
     * @param id 취소할 공유 링크의 ID
     * @return 성공적으로 취소된 경우 HTTP 200 OK 응답을 반환합니다.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Long id, Authentication auth) {
        service.revokeLink(id, auth);
        return ResponseEntity.ok().build();
    }
}
