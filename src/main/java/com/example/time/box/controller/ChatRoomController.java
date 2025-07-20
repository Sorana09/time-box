package com.example.time.box.controller;

import com.example.time.box.entity.ChatMessage;
import com.example.time.box.entity.RoomEntity;
import com.example.time.box.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final RoomService roomService;

    @GetMapping("/room/{token}")
    public ResponseEntity<RoomEntity> getChatRoom(@PathVariable String token) {
        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(token);

        if (roomOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RoomEntity room = roomOpt.get();
        if (room.getChatEnabled() == null || !room.getChatEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        return ResponseEntity.ok(room);
    }

    @PostMapping(value = "/room/{token}/join", consumes = {"application/json", "application/x-www-form-urlencoded", "text/plain", "*/*"})
    public ResponseEntity<RoomEntity> joinChatRoom(
            @PathVariable String token,
            @RequestBody String userEmail) {

        try {
            RoomEntity room = roomService.addParticipant(token, userEmail);
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping(value = "/room/{token}/message", consumes = {"application/json", "application/x-www-form-urlencoded", "text/plain", "*/*"})
    public ResponseEntity<Map<String, Object>> sendMessage(
            @PathVariable String token,
            @RequestParam String userEmail,
            @RequestParam String message) {

        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(token);

        if (roomOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RoomEntity room = roomOpt.get();

        if (!room.getParticipants().contains(userEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("sender", userEmail);
        response.put("roomToken", token);
        response.put("timestamp", OffsetDateTime.now());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/room/{token}/websocket-info")
    public ResponseEntity<Map<String, Object>> getWebSocketInfo(@PathVariable String token) {
        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(token);

        if (roomOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RoomEntity room = roomOpt.get();

        if (room.getChatEnabled() == null || !room.getChatEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Map<String, Object> wsInfo = new HashMap<>();
        wsInfo.put("endpoint", "/ws-chat");
        wsInfo.put("subscribeTopic", "/topic/public/" + token);
        wsInfo.put("sendMessageEndpoint", "/app/chat.sendMessage/" + token);
        wsInfo.put("addUserEndpoint", "/app/chat.addUser/" + token);

        return ResponseEntity.ok(wsInfo);
    }
}
