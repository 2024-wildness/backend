package com.madiest.moapin.reminder;

import com.madiest.moapin.auth.User;
import com.madiest.moapin.content.Content;
import com.madiest.moapin.content.ContentRepository;
import com.madiest.moapin.auth.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Service for managing reminders.
 */
@Service
public class ReminderService {
    private final ReminderRepository repository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    public ReminderService(ReminderRepository repository, ContentRepository contentRepository,
                           UserRepository userRepository) {
        this.repository = repository;
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Reminder createReminder(String username, Long contentId, Instant remindAt) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Reminder r = new Reminder();
        r.setContent(content);
        r.setUser(user);
        r.setRemindAt(remindAt);
        return repository.save(r);
    }

    public List<Reminder> listReminders(String username) {
        return repository.findByUserUsername(username);
    }

    @Transactional
    public void deleteReminder(Long id, String username) {
        Reminder r = repository.findById(id).orElseThrow();
        if (!r.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Unauthorized");
        }
        repository.delete(r);
    }
}
