package com.madiest.moapin.reminder.dto;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReminderRequest {
  private Long contentId;
  private Instant remindAt;
}
