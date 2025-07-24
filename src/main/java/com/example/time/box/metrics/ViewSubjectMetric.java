package com.example.time.box.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ViewSubjectMetric {

    private final MeterRegistry meterRegistry;

    public void createViewForSubject(Long subjectId) {
        Counter.builder("subjects.views")
                .description("views per subject")
                .tags(Tags.of(Tag.of("subject_id", subjectId.toString())))
                .register(meterRegistry)
                .increment();
    }

    public void countSubjectCreation() {
        Counter.builder("subjects.created")
                .description("number of subjects created")
                .tags(Tags.of(Tag.of("type", "created")))
                .register(meterRegistry)
                .increment();
    }

    public void countSubjectDeletion() {
        Counter.builder("subjects.deleted")
                .description("number of subjects deleted")
                .tags(Tags.of(Tag.of("type", "deleted")))
                .register(meterRegistry)
                .increment();
    }

    public void countDescriptionChange() {
        Counter.builder("subjects.description.changed")
                .description("number of subject description changes")
                .tags(Tags.of(Tag.of("type", "description_changed")))
                .register(meterRegistry)
                .increment();
    }

    public void countSessionsQuery(Long subjectId) {
        Counter.builder("subjects.sessions.queried")
                .description("number of queries for subject sessions")
                .tags(Tags.of(Tag.of("subject_id", subjectId.toString())))
                .register(meterRegistry)
                .increment();
    }

    public void countTimeAllottedQuery(Long subjectId) {
        Counter.builder("subjects.time.queried")
                .description("number of queries for subject time allotted")
                .tags(Tags.of(Tag.of("subject_id", subjectId.toString())))
                .register(meterRegistry)
                .increment();
    }

    public void registerSubjectForUser(Long userId, Long subjectId) {
        Counter.builder("user.subjects")
                .description("subjects per user")
                .tags(Tags.of(
                        Tag.of("user_id", userId.toString()),
                        Tag.of("subject_id", subjectId.toString())
                ))
                .register(meterRegistry)
                .increment();
    }
}
