package com.madiest.moapin.reminder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

/**
 * Repository for reminders.
 */
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByUserUsername(String username);

    @Query("select r from Reminder r where r.processed = false and r.remindAt <= ?1")
    List<Reminder> findDueReminders(Instant now);
}
