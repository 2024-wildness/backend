package com.madiest.moapin.reminder;

import com.madiest.moapin.auth.User;
import com.madiest.moapin.auth.UserRepository;
import com.madiest.moapin.content.Content;
import com.madiest.moapin.content.ContentRepository;
import com.madiest.moapin.content.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository test for Reminder.
 */
@DataJpaTest
class ReminderRepositoryTest {
    @Autowired ReminderRepository repository;
    @Autowired ContentRepository contentRepository;
    @Autowired UserRepository userRepository;

    @Test
    void saveAndQueryDue() {
        User u = new User();
        u.setUsername("rem");
        u.setEmail("rem@example.com");
        u.setPassword("x");
        userRepository.save(u);

        Note note = new Note();
        contentRepository.save(note);

        Reminder r = new Reminder();
        r.setUser(u);
        r.setContent(note);
        r.setRemindAt(Instant.now().minusSeconds(10));
        repository.save(r);

        List<Reminder> due = repository.findDueReminders(Instant.now());
        assertThat(due).hasSize(1);
    }
}
