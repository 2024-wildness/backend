package com.madiest.moapin.content;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for generic Content operations.
 */
public interface ContentRepository extends JpaRepository<Content, Long> {
    /**
     * List all content items belonging to the given username,
     * ordered with pinned items first and then by creation time descending.
     */
    java.util.List<Content> findByCategoryUserUsernameOrderByPinnedDescCreatedAtDesc(String username);
}