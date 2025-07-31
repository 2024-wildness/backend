package com.madiest.moapin.content;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for generic Content operations.
 */
public interface ContentRepository extends JpaRepository<Content, Long> {
}