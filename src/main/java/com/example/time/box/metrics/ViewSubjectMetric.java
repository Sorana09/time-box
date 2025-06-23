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
}
