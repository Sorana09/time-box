package com.example.time.box.controller;

import com.example.time.box.entity.ChatMessage;
import com.example.time.box.entity.RoomEntity;
import com.example.time.box.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final RoomService roomService;

    @MessageMapping("/chat.sendMessage/{roomToken}")
    @SendTo("/topic/public/{roomToken}")
    public ChatMessage sendMessage(@DestinationVariable String roomToken, @Payload ChatMessage chatMessage) {
        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(roomToken);

        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }

        RoomEntity room = roomOpt.get();

        if (!room.getParticipants().contains(chatMessage.getSender())) {
            throw new IllegalArgumentException("User is not a participant in this room");
        }

        chatMessage.setTimestamp(OffsetDateTime.now());
        chatMessage.setRoomToken(roomToken);

        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{roomToken}")
    @SendTo("/topic/public/{roomToken}")
    public ChatMessage addUser(@DestinationVariable String roomToken,
                               @Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {

        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(roomToken);

        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }

        RoomEntity room = roomOpt.get();

        if (!room.getParticipants().contains(chatMessage.getSender())) {
            roomService.addParticipant(roomToken, chatMessage.getSender());
        }

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("roomToken", roomToken);

        chatMessage.setTimestamp(OffsetDateTime.now());
        chatMessage.setRoomToken(roomToken);
        chatMessage.setType(ChatMessage.MessageType.JOIN);

        return chatMessage;
    }
}