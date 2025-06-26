package com.example.time.box.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewAchievementMetric {
    private final MeterRegistry meterRegistry;

    public void countAchievementCreation() {
        Counter.builder("achievements.created")
                .description("number of achievements created")
                .tags(Tags.of(Tag.of("type", "created")))
                .register(meterRegistry)
                .increment();
    }
    public void countAchievementCompleted() {
        Counter.builder("achievements.completed")
                .description("number of achievements completed")
                .tags(Tags.of(Tag.of("type", "completed")))
                .register(meterRegistry)
                .increment();
    }
}
