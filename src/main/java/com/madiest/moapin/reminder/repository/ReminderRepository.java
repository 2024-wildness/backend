package com.madiest.moapin.reminder.repository;

import com.madiest.moapin.reminder.model.Reminder;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

  @Query("SELECT r FROM Reminder r WHERE r.remindAt <= :now AND r.processed = false")
  List<Reminder> findDueReminders(Instant now);
}
