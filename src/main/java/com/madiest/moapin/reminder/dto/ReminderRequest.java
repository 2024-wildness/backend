package com.madiest.moapin.reminder.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class ReminderRequest {
    private Long contentId;
    private Instant remindAt;
}