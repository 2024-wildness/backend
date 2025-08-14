package com.madiest.moapin.search.repository;

import com.madiest.moapin.search.model.OutboxEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

  List<OutboxEvent> findByProcessedFalse();
}
