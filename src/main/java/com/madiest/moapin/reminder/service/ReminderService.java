package com.madiest.moapin.reminder.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReminderService {

  @Transactional
  public void createReminder(Long contentId, LocalDateTime remindAt) {
    // TODO: Implement reminder creation logic
  }

  @Transactional
  public void deleteReminder(Long reminderId) {
    // TODO: Implement reminder deletion logic
  }

  @Transactional(readOnly = true)
  public void getReminders() {
    // TODO: Implement get reminders logic
  }
}
