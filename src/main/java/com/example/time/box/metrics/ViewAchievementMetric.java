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

    public void countAchievementDeleted() {
        Counter.builder("achievements.deleted")
                .description("number of achievements deleted")
                .tags(Tags.of(Tag.of("type", "deleted")))
                .register(meterRegistry)
                .increment();
    }

    public void countAchievementViewed() {
        Counter.builder("achievements.viewed")
                .description("number of achievement views")
                .tags(Tags.of(Tag.of("type", "viewed")))
                .register(meterRegistry)
                .increment();
    }

    public void registerAchievementForUser(Long userId, Long achievementId) {
        Counter.builder("user.achievements")
                .description("achievements per user")
                .tags(Tags.of(
                        Tag.of("user_id", userId.toString()),
                        Tag.of("achievement_id", achievementId.toString())
                ))
                .register(meterRegistry)
                .increment();
    }
}
