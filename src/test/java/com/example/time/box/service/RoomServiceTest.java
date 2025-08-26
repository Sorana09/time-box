package com.example.time.box.service;

import com.example.time.box.entity.RoomEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.CreateRoomRequest;
import com.example.time.box.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRoom_ShouldCreateRoomWithInvitationToken() {
        // Arrange
        CreateRoomRequest request = new CreateRoomRequest("user@example.com", "Test room");

        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");

        when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        RoomEntity savedRoom = new RoomEntity();
        savedRoom.setId(1L);
        savedRoom.setDescription(request.getDescription());
        savedRoom.setCreatedBy(user.getEmail());
        savedRoom.setInvitationToken("test-token");
        savedRoom.setIsActive(true);
        savedRoom.setChatEnabled(true);
        savedRoom.setParticipants(new ArrayList<>());
        savedRoom.getParticipants().add(user.getEmail());

        when(roomRepository.save(any(RoomEntity.class))).thenReturn(savedRoom);

        // Act
        RoomEntity result = roomService.createRoom(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(user.getEmail(), result.getCreatedBy());
        assertNotNull(result.getInvitationToken());
        assertTrue(result.getIsActive());
        assertTrue(result.getChatEnabled());
        assertTrue(result.getParticipants().contains(user.getEmail()));
        verify(roomRepository, times(1)).save(any(RoomEntity.class));
    }

    @Test
    void findByInvitationToken_ShouldReturnRoom_WhenRoomExists() {
        // Arrange
        String token = "test-token";

        RoomEntity room = new RoomEntity();
        room.setId(1L);
        room.setInvitationToken(token);

        when(roomRepository.findByInvitationToken(token)).thenReturn(Optional.of(room));

        // Act
        Optional<RoomEntity> result = roomService.findByInvitationToken(token);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(token, result.get().getInvitationToken());
        verify(roomRepository, times(1)).findByInvitationToken(token);
    }

    @Test
    void addParticipant_ShouldAddUserToRoom() {
        // Arrange
        String token = "test-token";
        String userEmail = "user@example.com";

        RoomEntity room = new RoomEntity();
        room.setId(1L);
        room.setInvitationToken(token);
        room.setParticipants(new ArrayList<>());

        when(roomRepository.findByInvitationToken(token)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(RoomEntity.class))).thenReturn(room);

        // Act
        RoomEntity result = roomService.addParticipant(token, userEmail);

        // Assert
        assertNotNull(result);
        assertTrue(result.getParticipants().contains(userEmail));
        verify(roomRepository, times(1)).findByInvitationToken(token);
        verify(roomRepository, times(1)).save(any(RoomEntity.class));
    }

    @Test
    void addParticipant_ShouldThrowException_WhenRoomNotFound() {
        // Arrange
        String token = "test-token";
        String userEmail = "user@example.com";

        when(roomRepository.findByInvitationToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.addParticipant(token, userEmail);
        });

        assertEquals("Room with invitation token " + token + " does not exist.", exception.getMessage());
        verify(roomRepository, times(1)).findByInvitationToken(token);
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }
}