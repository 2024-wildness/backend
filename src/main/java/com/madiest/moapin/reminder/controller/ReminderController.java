package com.madiest.moapin.reminder.controller;

import com.madiest.moapin.reminder.dto.ReminderCreateRequest;
import com.madiest.moapin.reminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

  private final ReminderService reminderService;

  @PostMapping
  public ResponseEntity<Void> createReminder(@RequestBody ReminderCreateRequest request) {
    // TODO: Implement reminder creation
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{reminderId}")
  public ResponseEntity<Void> deleteReminder(@PathVariable Long reminderId) {
    // TODO: Implement reminder deletion
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<Void> getReminders() {
    // TODO: Implement get reminders
    return ResponseEntity.ok().build();
  }
}
