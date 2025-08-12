package com.madiest.moapin.reminder.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReminderCreateRequest {
    private Long contentId;
    private LocalDateTime remindAt;
}
