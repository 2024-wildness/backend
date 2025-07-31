package com.madiest.moapin.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sesv2.SesAsyncClient;
import software.amazon.awssdk.services.sesv2.model.*;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Background job processing due reminders.
 */
@Component
public class ReminderScheduler {
    private static final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);
    private final ReminderRepository repository;
    private final SesAsyncClient sesClient;

    public ReminderScheduler(ReminderRepository repository, SesAsyncClient sesClient) {
        this.repository = repository;
        this.sesClient = sesClient;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void processReminders() {
        List<Reminder> due = repository.findDueReminders(Instant.now());
        for (Reminder r : due) {
            try {
                sendEmail(r).join();
                r.setProcessed(true);
            } catch (Exception e) {
                log.error("Failed to send reminder {}", r.getId(), e);
            }
        }
        repository.saveAll(due);
    }

    private CompletableFuture<SendEmailResponse> sendEmail(Reminder r) {
        // simple text email
        Destination destination = Destination.builder()
                .toAddresses(r.getUser().getEmail())
                .build();
        Content subject = Content.builder().data("Reminder").build();
        Content text = Content.builder().data("You asked to be reminded about content " + r.getContent().getId()).build();
        Body body = Body.builder().text(text).build();
        Message message = Message.builder().subject(subject).body(body).build();
        SendEmailRequest req = SendEmailRequest.builder()
                .destination(destination)
                .fromEmailAddress("noreply@example.com")
                .content(EmailContent.builder().simple(message).build())
                .build();
        return sesClient.sendEmail(req);
    }
}
