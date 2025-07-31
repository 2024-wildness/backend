package com.madiest.moapin.share;

import org.springframework.data.jpa.repository.JpaRepository;
import com.madiest.moapin.content.Content;

import java.util.Optional;

/** Repository for share links. */
public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {
    Optional<ShareLink> findByToken(String token);
    java.util.List<ShareLink> findByContent(Content content);
}
