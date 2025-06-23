package com.example.time.box.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ViewUserMetric {
    private final MeterRegistry meterRegistry;

    public void registerViewForUser(Long userId) {
        Counter.builder("user.views")
                .description("views per user")
                .tags(Tags.of(Tag.of("user_id", userId.toString())))
                .register(meterRegistry)
                .increment();
    }

    public void countUserRegistration() {
        Counter.builder("users.registered")
                .description("number of users registered")
                .tags(Tags.of(Tag.of("type", "registered")))
                .register(meterRegistry)
                .increment();
    }
}
