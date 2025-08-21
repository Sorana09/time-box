package com.example.time.box.event;

import com.example.time.box.domain.factory.ChatMessageFactory;
import com.example.time.box.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageFactory chatMessageFactory;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomToken = (String) headerAccessor.getSessionAttributes().get("roomToken");

        if (username != null && roomToken != null) {
            log.info("User Disconnected: {}", username);

            ChatMessage chatMessage = chatMessageFactory.createLeaveMessage(username, roomToken);
            messagingTemplate.convertAndSend("/topic/public/" + roomToken, chatMessage);
        }
    }
}