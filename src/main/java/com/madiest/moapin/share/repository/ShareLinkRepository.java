package com.madiest.moapin.share.repository;

import com.madiest.moapin.share.model.ShareLink;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareLinkRepository extends JpaRepository<ShareLink, String> {

  Optional<ShareLink> findByToken(String token);
}
