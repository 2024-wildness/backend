package com.madiest.moapin.reminder;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.auth.repository.UserRepository;
import com.madiest.moapin.category.model.Category;
import com.madiest.moapin.category.repository.CategoryRepository;
import com.madiest.moapin.content.model.Content;
import com.madiest.moapin.content.repository.ContentRepository;
import com.madiest.moapin.content.model.Note;
import com.madiest.moapin.reminder.model.Reminder;
import com.madiest.moapin.reminder.repository.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReminderRepositoryTest {
    @Autowired private ReminderRepository reminderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ContentRepository contentRepository;

    private User user;
    private Content content;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("rem");
        user.setEmail("rem@example.com");
        user.setPassword("x");
        userRepository.save(user);

        Category category = new Category("Reminder Category", user);
        categoryRepository.save(category);

        Note note = new Note("Test Note", user, category, "Test note for reminder");
        content = contentRepository.save(note);
    }

    @Test
    @Transactional
    void saveAndQueryDue() {
        Reminder r = new Reminder(content, user, Instant.now().minusSeconds(10));
        reminderRepository.save(r);

        List<Reminder> due = reminderRepository.findDueReminders(Instant.now());
        assertThat(due).hasSize(1);
        assertThat(due.get(0).getUser().getUsername()).isEqualTo("rem");
    }
}

