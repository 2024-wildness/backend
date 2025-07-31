package com.madiest.moapin.search;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for OutboxEvent.
 */
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByProcessedFalse();
}
