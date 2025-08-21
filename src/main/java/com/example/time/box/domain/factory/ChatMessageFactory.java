package com.example.time.box.domain.factory;

import com.example.time.box.entity.ChatMessage;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;


@Component
public class ChatMessageFactory {

    public ChatMessage createChatMessage(String content, String sender, String roomToken) {
        return ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .content(content)
                .sender(sender)
                .roomToken(roomToken)
                .timestamp(OffsetDateTime.now())
                .build();
    }

    public ChatMessage createJoinMessage(String sender, String roomToken) {
        return ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .sender(sender)
                .roomToken(roomToken)
                .timestamp(OffsetDateTime.now())
                .build();
    }

    public ChatMessage createLeaveMessage(String sender, String roomToken) {
        return ChatMessage.builder()
                .type(ChatMessage.MessageType.LEAVE)
                .sender(sender)
                .roomToken(roomToken)
                .timestamp(OffsetDateTime.now())
                .build();
    }
}
