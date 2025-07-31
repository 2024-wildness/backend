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

    /**
     * ShareLinkService의 인스턴스를 생성한다.
     *
     * @param repository        공유 링크 정보를 관리하는 저장소
     * @param contentRepository 콘텐츠 정보를 관리하는 저장소
     */
    public ShareLinkService(ShareLinkRepository repository,
                            ContentRepository contentRepository) {
        this.repository = repository;
        this.contentRepository = contentRepository;
    }

    /**
     * 지정된 콘텐츠에 대한 새로운 공유 링크를 생성합니다.
     *
     * 콘텐츠 소유자임이 인증된 사용자만 공유 링크를 생성할 수 있습니다. 만료 시각과 최대 다운로드 횟수를 설정할 수 있습니다.
     *
     * @param contentId 공유 링크를 생성할 콘텐츠의 ID
     * @param expiresAt 공유 링크의 만료 시각 (null일 경우 만료 없음)
     * @param maxDownloads 공유 링크의 최대 다운로드 횟수 (null일 경우 제한 없음)
     * @param auth 인증된 사용자 정보
     * @return 생성된 공유 링크 객체
     * @throws ResponseStatusException 콘텐츠가 존재하지 않거나, 사용자가 소유자가 아닌 경우 각각 404 또는 403 예외가 발생합니다.
     */
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

    /**
     * 주어진 토큰으로 공유 링크를 통해 콘텐츠에 접근합니다.
     *
     * 공유 링크가 존재하지 않거나, 만료되었거나, 다운로드 제한을 초과한 경우 예외가 발생합니다.
     *
     * @param token 접근할 공유 링크의 토큰
     * @return 공유 링크와 연결된 콘텐츠
     * @throws ResponseStatusException 링크가 존재하지 않으면 404 NOT_FOUND, 만료 또는 다운로드 제한 초과 시 400 BAD_REQUEST 예외를 발생시킵니다.
     */
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

    /**
     * 지정된 콘텐츠에 대한 모든 공유 링크 목록을 반환합니다.
     *
     * @param contentId 공유 링크를 조회할 콘텐츠의 ID
     * @param auth      요청 사용자의 인증 정보
     * @return 해당 콘텐츠에 연결된 공유 링크 목록
     * @throws ResponseStatusException 콘텐츠가 존재하지 않거나 접근 권한이 없는 경우 각각 404 또는 403 예외가 발생합니다.
     */
    @Transactional
    public List<ShareLink> listShareLinks(Long contentId, Authentication auth) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (content.getCategory() != null && !content.getCategory().getUser().getUsername().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return repository.findByContent(content);
    }

    /**
     * 지정된 공유 링크를 소유자가 직접 폐기(무효화)합니다.
     *
     * @param id 폐기할 공유 링크의 ID
     * @param auth 현재 인증된 사용자 정보
     * @throws ResponseStatusException 링크가 존재하지 않거나, 소유자가 아닌 경우 각각 404 또는 403 예외가 발생합니다.
     */
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
