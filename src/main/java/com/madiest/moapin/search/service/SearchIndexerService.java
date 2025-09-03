package com.madiest.moapin.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madiest.moapin.search.model.OutboxEvent;
import com.madiest.moapin.search.repository.OutboxEventRepository;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/** Background job that processes outbox events and updates MeiliSearch. */
@Service
@ConditionalOnBean(Client.class)
public class SearchIndexerService {
  private final OutboxEventRepository repository;
    private final Optional<Client> client;
  private final ObjectMapper mapper = new ObjectMapper();

    public SearchIndexerService(OutboxEventRepository repository, Optional<Client> client) {
    this.repository = repository;
    this.client = client;
  }

  @Scheduled(fixedDelay = 5000)
  @Transactional
  public void processEvents() {
      if (client.isEmpty()) return; // search disabled
    List<OutboxEvent> events = repository.findByProcessedFalse();
    if (events.isEmpty()) return;
      Index index = client.get().index("contents");
    for (OutboxEvent e : events) {
      try {
        if (e.getOperation() == OutboxEvent.Operation.DELETE) {
          index.deleteDocument(e.getContentId().toString());
        } else {
          index.addDocuments(e.getPayload(), new JacksonJsonHandler().toString());
        }
        e.setProcessed(true);
      } catch (Exception ex) {
        // leave event unprocessed for retry
      }
    }
    repository.saveAll(events);
  }
}
