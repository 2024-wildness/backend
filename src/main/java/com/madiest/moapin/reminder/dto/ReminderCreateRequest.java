package com.madiest.moapin.reminder.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReminderCreateRequest {
  private Long contentId;
  private LocalDateTime remindAt;
}
