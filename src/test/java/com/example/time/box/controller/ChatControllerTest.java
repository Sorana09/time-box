package com.example.time.box.controller;

import com.example.time.box.entity.ChatMessage;
import com.example.time.box.entity.RoomEntity;
import com.example.time.box.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_ShouldReturnMessage_WhenUserIsParticipant() {
        // Arrange
        String roomToken = "test-token";
        String userEmail = "user@example.com";
        
        RoomEntity room = new RoomEntity();
        room.setInvitationToken(roomToken);
        room.setParticipants(new ArrayList<>());
        room.getParticipants().add(userEmail);
        
        when(roomService.findByInvitationToken(roomToken)).thenReturn(Optional.of(room));
        
        ChatMessage inputMessage = new ChatMessage();
        inputMessage.setSender(userEmail);
        inputMessage.setContent("Hello, world!");
        inputMessage.setType(ChatMessage.MessageType.CHAT);
        
        // Act
        ChatMessage result = chatController.sendMessage(roomToken, inputMessage);
        
        // Assert
        assertNotNull(result);
        assertEquals(userEmail, result.getSender());
        assertEquals("Hello, world!", result.getContent());
        assertEquals(ChatMessage.MessageType.CHAT, result.getType());
        assertEquals(roomToken, result.getRoomToken());
        assertNotNull(result.getTimestamp());
        
        verify(roomService, times(1)).findByInvitationToken(roomToken);
    }
    
    @Test
    void sendMessage_ShouldThrowException_WhenRoomNotFound() {
        // Arrange
        String roomToken = "test-token";
        String userEmail = "user@example.com";
        
        when(roomService.findByInvitationToken(roomToken)).thenReturn(Optional.empty());
        
        ChatMessage inputMessage = new ChatMessage();
        inputMessage.setSender(userEmail);
        inputMessage.setContent("Hello, world!");
        inputMessage.setType(ChatMessage.MessageType.CHAT);
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            chatController.sendMessage(roomToken, inputMessage);
        });
        
        assertEquals("Room not found", exception.getMessage());
        verify(roomService, times(1)).findByInvitationToken(roomToken);
    }
    
    @Test
    void sendMessage_ShouldThrowException_WhenUserNotParticipant() {
        // Arrange
        String roomToken = "test-token";
        String userEmail = "user@example.com";
        String otherEmail = "other@example.com";
        
        RoomEntity room = new RoomEntity();
        room.setInvitationToken(roomToken);
        room.setParticipants(new ArrayList<>());
        room.getParticipants().add(otherEmail); // User is not a participant
        
        when(roomService.findByInvitationToken(roomToken)).thenReturn(Optional.of(room));
        
        ChatMessage inputMessage = new ChatMessage();
        inputMessage.setSender(userEmail);
        inputMessage.setContent("Hello, world!");
        inputMessage.setType(ChatMessage.MessageType.CHAT);
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            chatController.sendMessage(roomToken, inputMessage);
        });
        
        assertEquals("User is not a participant in this room", exception.getMessage());
        verify(roomService, times(1)).findByInvitationToken(roomToken);
    }
    
    @Test
    void addUser_ShouldAddUserToRoom_WhenRoomExists() {
        // Arrange
        String roomToken = "test-token";
        String userEmail = "user@example.com";
        
        RoomEntity room = new RoomEntity();
        room.setInvitationToken(roomToken);
        room.setParticipants(new ArrayList<>());
        
        when(roomService.findByInvitationToken(roomToken)).thenReturn(Optional.of(room));
        when(roomService.addParticipant(anyString(), anyString())).thenReturn(room);
        
        ChatMessage inputMessage = new ChatMessage();
        inputMessage.setSender(userEmail);
        inputMessage.setContent("User joined");
        
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        Map<String, Object> sessionAttributes = new HashMap<>();
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        
        // Act
        ChatMessage result = chatController.addUser(roomToken, inputMessage, headerAccessor);
        
        // Assert
        assertNotNull(result);
        assertEquals(userEmail, result.getSender());
        assertEquals(ChatMessage.MessageType.JOIN, result.getType());
        assertEquals(roomToken, result.getRoomToken());
        assertNotNull(result.getTimestamp());
        
        assertEquals(userEmail, sessionAttributes.get("username"));
        assertEquals(roomToken, sessionAttributes.get("roomToken"));
        
        verify(roomService, times(1)).findByInvitationToken(roomToken);
        verify(roomService, times(1)).addParticipant(roomToken, userEmail);
    }
    
    @Test
    void addUser_ShouldThrowException_WhenRoomNotFound() {
        // Arrange
        String roomToken = "test-token";
        String userEmail = "user@example.com";
        
        when(roomService.findByInvitationToken(roomToken)).thenReturn(Optional.empty());
        
        ChatMessage inputMessage = new ChatMessage();
        inputMessage.setSender(userEmail);
        inputMessage.setContent("User joined");
        
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            chatController.addUser(roomToken, inputMessage, headerAccessor);
        });
        
        assertEquals("Room not found", exception.getMessage());
        verify(roomService, times(1)).findByInvitationToken(roomToken);
        verify(roomService, never()).addParticipant(anyString(), anyString());
    }
}