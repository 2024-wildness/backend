package com.madiest.moapin.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic repository test ensuring OutboxEvent persistence.
 */
@DataJpaTest
class OutboxEventRepositoryTest {

    @Autowired
    private OutboxEventRepository repository;

    @Test
    void saveAndRetrieve() {
        OutboxEvent evt = new OutboxEvent();
        evt.setType("INDEX");
        evt.setContentId(1L);
        evt.setPayload("{\"id\":1}");
        OutboxEvent saved = repository.save(evt);
        assertThat(repository.findById(saved.getId())).isPresent();
    }
}
