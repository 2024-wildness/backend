package com.madiest.moapin.search;

import static org.assertj.core.api.Assertions.assertThat;

import com.madiest.moapin.search.model.OutboxEvent;
import com.madiest.moapin.search.repository.OutboxEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/** Basic repository test ensuring OutboxEvent persistence. */
@DataJpaTest
@ActiveProfiles("test")
class OutboxEventRepositoryTest {

  @Autowired private OutboxEventRepository repository;

  @Test
  void saveAndRetrieve() {
    OutboxEvent evt = new OutboxEvent(1L, OutboxEvent.Operation.CREATE, "{\"id\":1}");
    OutboxEvent saved = repository.save(evt);
    assertThat(repository.findById(saved.getId())).isPresent();
  }
}
