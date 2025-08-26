package com.example.time.box.controller;

import com.example.time.box.entity.RoomEntity;
import com.example.time.box.service.RoomService;
import com.example.time.box.service.SubjectSessionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/shared-session")
@RequiredArgsConstructor
@Slf4j
public class SharedSessionController {

    private final RoomService roomService;
    private final SubjectSessionService subjectSessionService;

    private final Map<String, SharedSessionMessage> lastStateByRoom = new ConcurrentHashMap<>();

    @MessageMapping("/session.start/{roomToken}")
    @SendTo("/topic/session/{roomToken}")
    public SharedSessionMessage start(@DestinationVariable String roomToken, @Payload SharedSessionMessage msg) {
        validateParticipant(roomToken, msg.getUser());
        Long broadcastSubject = msg.getSubjectId();
        try {
            if (broadcastSubject != null) {
                subjectSessionService.setStartTime(broadcastSubject);
            }
        } catch (Exception e) {
            log.warn("Failed to create backend subject session for subjectId={} in room {}: {}", msg.getSubjectId(), roomToken, e.getMessage());
        }

        SharedSessionMessage out = new SharedSessionMessage();
        out.setAction("start");
        out.setServerTime(OffsetDateTime.now().toString());
        out.setStartTime(out.getServerTime());
        out.setUser(msg.getUser());
        out.setSubjectId(broadcastSubject);
        lastStateByRoom.put(roomToken, out);
        return out;
    }

    @MessageMapping("/session.pause/{roomToken}")
    @SendTo("/topic/session/{roomToken}")
    public SharedSessionMessage pause(@DestinationVariable String roomToken, @Payload SharedSessionMessage msg) {
        validateParticipant(roomToken, msg.getUser());
        SharedSessionMessage out = new SharedSessionMessage();
        out.setAction("pause");
        out.setServerTime(OffsetDateTime.now().toString());
        out.setUser(msg.getUser());
        lastStateByRoom.put(roomToken, out);
        return out;
    }

    @MessageMapping("/session.resume/{roomToken}")
    @SendTo("/topic/session/{roomToken}")
    public SharedSessionMessage resume(@DestinationVariable String roomToken, @Payload SharedSessionMessage msg) {
        validateParticipant(roomToken, msg.getUser());
        SharedSessionMessage out = new SharedSessionMessage();
        out.setAction("resume");
        out.setServerTime(OffsetDateTime.now().toString());
        out.setStartTime(msg.getStartTime());
        out.setUser(msg.getUser());
        lastStateByRoom.put(roomToken, out);
        return out;
    }

    @MessageMapping("/session.end/{roomToken}")
    @SendTo("/topic/session/{roomToken}")
    public SharedSessionMessage end(@DestinationVariable String roomToken, @Payload SharedSessionMessage msg) {
        validateParticipant(roomToken, msg.getUser());
        SharedSessionMessage out = new SharedSessionMessage();
        out.setAction("end");
        out.setServerTime(OffsetDateTime.now().toString());
        out.setUser(msg.getUser());
        lastStateByRoom.put(roomToken, out);
        return out;
    }

    private void validateParticipant(String roomToken, String user) {
        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(roomToken);
        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }
        RoomEntity room = roomOpt.get();
        List<String> participants = new ArrayList<>();
        if (room.getParticipants() != null) {
            participants.addAll(room.getParticipants());
        }
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }
        if (!participants.contains(user)) {
            try {
                roomService.addParticipant(roomToken, user);
            } catch (Exception ignored) {
            }
        }
    }

    @GetMapping("/{roomToken}/status")
    public ResponseEntity<SharedSessionMessage> getStatus(@PathVariable String roomToken) {
        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(roomToken);
        if (roomOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        SharedSessionMessage last = lastStateByRoom.get(roomToken);
        if (last == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(last);
    }

    @Data
    @AllArgsConstructor
    public static class SharedSessionMessage {
        private String action;
        private String startTime;
        private String serverTime;
        private String user;
        private Long subjectId;
        public SharedSessionMessage() {
        }
    }
}


