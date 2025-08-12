package com.madiest.moapin.share.repository;

import com.madiest.moapin.share.model.ShareLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareLinkRepository extends JpaRepository<ShareLink, String> {

    Optional<ShareLink> findByToken(String token);
}
