package com.madiest.moapin.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Background job that processes outbox events and updates MeiliSearch.
 */
@Service
public class SearchIndexerService {
    private final OutboxEventRepository repository;
    private final Client client;
    private final ObjectMapper mapper = new ObjectMapper();

    public SearchIndexerService(OutboxEventRepository repository, Client client) {
        this.repository = repository;
        this.client = client;
    }

    /**
     * 처리되지 않은 Outbox 이벤트를 주기적으로 조회하여 MeiliSearch "contents" 인덱스를 업데이트합니다.
     *
     * 이벤트 유형이 "DELETE"인 경우 해당 콘텐츠 ID의 문서를 인덱스에서 삭제하고, 그 외에는 페이로드를 문서로 추가합니다.
     * 처리에 성공한 이벤트는 처리 완료로 표시되며, 실패한 이벤트는 다음 실행 시 재시도됩니다.
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processEvents() {
        List<OutboxEvent> events = repository.findByProcessedFalse();
        if (events.isEmpty()) return;
        Index index = client.index("contents");
        for (OutboxEvent e : events) {
            try {
                if ("DELETE".equals(e.getType())) {
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
