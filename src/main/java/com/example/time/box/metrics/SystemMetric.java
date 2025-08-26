package com.example.time.box.metrics;

import io.micrometer.core.instrument.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SystemMetric {
    private final MeterRegistry meterRegistry;

    public void countApiCall(String endpoint, String method) {
        Counter.builder("api.calls")
                .description("API endpoint calls")
                .tags(Tags.of(
                        Tag.of("endpoint", endpoint),
                        Tag.of("method", method)
                ))
                .register(meterRegistry)
                .increment();
    }

    public void countErrorOccurrence(String errorType) {
        Counter.builder("errors.occurred")
                .description("error occurrences by type")
                .tags(Tags.of(Tag.of("error_type", errorType)))
                .register(meterRegistry)
                .increment();
    }

    public void recordOperationTime(String operation, long timeInMs) {
        Timer.builder("operation.time")
                .description("time taken for operations")
                .tags(Tags.of(Tag.of("operation", operation)))
                .register(meterRegistry)
                .record(timeInMs, TimeUnit.MILLISECONDS);
    }

    public Timer startTimerForOperation(String operation) {
        return Timer.builder("operation.time")
                .description("time taken for operations")
                .tags(Tags.of(Tag.of("operation", operation)))
                .register(meterRegistry);
    }

    public void countDatabaseQuery(String queryType) {
        Counter.builder("database.queries")
                .description("database queries by type")
                .tags(Tags.of(Tag.of("query_type", queryType)))
                .register(meterRegistry)
                .increment();
    }

    public void countActiveUsers() {
        Counter.builder("users.active")
                .description("number of active users")
                .register(meterRegistry)
                .increment();
    }
}