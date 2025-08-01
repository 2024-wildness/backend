package com.madiest.moapin.reminder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST API for managing reminders.
 */
@RestController
@RequestMapping("/api/reminders")
public class ReminderController {
    private final ReminderService service;

    public ReminderController(ReminderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reminder create(@RequestBody ReminderRequest req, Authentication auth) {
        return service.createReminder(auth.getName(), req.getContentId(), req.getRemindAt());
    }

    @GetMapping
    public List<Reminder> list(Authentication auth) {
        return service.listReminders(auth.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication auth) {
        service.deleteReminder(id, auth.getName());
    }

    @Setter
    @Getter
    public static class ReminderRequest {
        private Long contentId;
        private Instant remindAt;

    }
}
