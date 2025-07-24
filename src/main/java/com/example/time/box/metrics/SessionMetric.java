package com.example.time.box.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionMetric {
    private final MeterRegistry meterRegistry;

    public void countSessionCreation() {
        Counter.builder("sessions.created")
                .description("number of sessions created")
                .tags(Tags.of(Tag.of("type", "created")))
                .register(meterRegistry)
                .increment();
    }

    public void countSessionDeletion() {
        Counter.builder("sessions.deleted")
                .description("number of sessions deleted")
                .tags(Tags.of(Tag.of("type", "deleted")))
                .register(meterRegistry)
                .increment();
    }

    public void countSessionExpiration() {
        Counter.builder("sessions.expired")
                .description("number of sessions expired")
                .tags(Tags.of(Tag.of("type", "expired")))
                .register(meterRegistry)
                .increment();
    }

    public void registerSessionForUser(Long userId) {
        Counter.builder("user.sessions")
                .description("sessions per user")
                .tags(Tags.of(Tag.of("user_id", userId.toString())))
                .register(meterRegistry)
                .increment();
    }
}