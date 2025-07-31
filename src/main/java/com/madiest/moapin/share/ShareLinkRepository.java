package com.madiest.moapin.share;

import org.springframework.data.jpa.repository.JpaRepository;
import com.madiest.moapin.content.Content;

import java.util.Optional;
import java.util.List;
/** Repository for share links. */
public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {
    /**
 * 주어진 토큰 값으로 공유 링크 엔티티를 조회합니다.
 *
 * @param token 조회할 공유 링크의 토큰 값
 * @return 해당 토큰에 해당하는 공유 링크가 존재하면 Optional로 반환하며, 없으면 빈 Optional을 반환합니다.
 */
Optional<ShareLink> findByToken(String token);
    /**
 * 지정된 콘텐츠와 연관된 모든 공유 링크 엔티티 목록을 반환합니다.
 *
 * @param content 공유 링크를 조회할 대상 콘텐츠
 * @return 해당 콘텐츠에 연결된 모든 공유 링크의 리스트
 */
List<ShareLink> findByContent(Content content);
}
