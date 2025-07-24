package com.example.time.box.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomMetric {
    private final MeterRegistry meterRegistry;

    public void countRoomCreation() {
        Counter.builder("rooms.created")
                .description("number of rooms created")
                .tags(Tags.of(Tag.of("type", "created")))
                .register(meterRegistry)
                .increment();
    }

    public void countParticipantAdded() {
        Counter.builder("rooms.participants.added")
                .description("number of participants added to rooms")
                .tags(Tags.of(Tag.of("type", "added")))
                .register(meterRegistry)
                .increment();
    }

    public void registerRoomAccess(String invitationToken) {
        Counter.builder("rooms.access")
                .description("room access by invitation token")
                .tags(Tags.of(Tag.of("invitation_token", invitationToken)))
                .register(meterRegistry)
                .increment();
    }

    public void countRoomStatusChange(boolean isActive) {
        String status = isActive ? "activated" : "deactivated";
        Counter.builder("rooms.status.changed")
                .description("room status changes")
                .tags(Tags.of(Tag.of("status", status)))
                .register(meterRegistry)
                .increment();
    }

    public void countChatStatusChange(boolean chatEnabled) {
        String status = chatEnabled ? "enabled" : "disabled";
        Counter.builder("rooms.chat.status")
                .description("room chat status changes")
                .tags(Tags.of(Tag.of("status", status)))
                .register(meterRegistry)
                .increment();
    }
}