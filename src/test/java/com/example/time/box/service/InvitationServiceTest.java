package com.example.time.box.service;

import com.example.time.box.entity.InvitationEntity;
import com.example.time.box.entity.RoomEntity;
import com.example.time.box.entity.request.CreateInvitationRequest;
import com.example.time.box.repository.InvitationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvitationServiceTest {

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private InvitationService invitationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInvitation_ShouldCreateInvitationWithToken() {
        // Arrange
        CreateInvitationRequest request = new CreateInvitationRequest();
        request.setInvitedEmail("test@example.com");
        request.setDescription("Test invitation");
        request.setInvitedBy("sender@example.com");

        InvitationEntity savedInvitation = new InvitationEntity();
        savedInvitation.setId(1L);
        savedInvitation.setInvitedEmail(request.getInvitedEmail());
        savedInvitation.setDescription(request.getDescription());
        savedInvitation.setInvitedBy(request.getInvitedBy());
        savedInvitation.setAccepted(false);
        savedInvitation.setInvitationToken(UUID.randomUUID().toString());

        when(invitationRepository.save(any(InvitationEntity.class))).thenReturn(savedInvitation);


        InvitationEntity result = invitationService.createInvitation(request);

        assertNotNull(result);
        assertEquals(request.getInvitedEmail(), result.getInvitedEmail());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getInvitedBy(), result.getInvitedBy());
        assertFalse(result.getAccepted());
        assertNotNull(result.getInvitationToken());
        verify(invitationRepository, times(1)).save(any(InvitationEntity.class));
    }

    @Test
    void acceptInvitation_ShouldAcceptInvitationAndCreateRoom() {
        // Arrange
        String token = UUID.randomUUID().toString();
        String userEmail = "test@example.com";

        InvitationEntity invitation = new InvitationEntity();
        invitation.setId(1L);
        invitation.setInvitedEmail(userEmail);
        invitation.setDescription("Test invitation");
        invitation.setInvitedBy("sender@example.com");
        invitation.setAccepted(false);
        invitation.setInvitationToken(token);

        when(invitationRepository.findByInvitationToken(token)).thenReturn(Optional.of(invitation));
        when(invitationRepository.save(any(InvitationEntity.class))).thenReturn(invitation);
        when(roomService.findByInvitationToken(token)).thenReturn(Optional.empty());

        RoomEntity newRoom = new RoomEntity();
        newRoom.setId(1L);
        newRoom.setInvitationToken(token);

        newRoom.setDescription(invitation.getDescription());
        newRoom.setCreatedBy(invitation.getInvitedBy());
        newRoom.setIsActive(true);
        newRoom.setChatEnabled(true);
        newRoom.setParticipants(new ArrayList<>());
        
        when(roomService.saveRoom(any(RoomEntity.class))).thenReturn(newRoom);

        // Act
        InvitationEntity result = invitationService.acceptInvitation(token, userEmail);

        // Assert
        assertNotNull(result);
        assertTrue(result.getAccepted());
        verify(invitationRepository, times(1)).findByInvitationToken(token);
        verify(invitationRepository, times(1)).save(any(InvitationEntity.class));
        verify(roomService, times(1)).findByInvitationToken(token);
        verify(roomService, times(1)).saveRoom(any(RoomEntity.class));
    }

    @Test
    void acceptInvitation_ShouldThrowException_WhenInvitationNotFound() {
        // Arrange
        String token = UUID.randomUUID().toString();
        String userEmail = "test@example.com";

        when(invitationRepository.findByInvitationToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            invitationService.acceptInvitation(token, userEmail);
        });

        assertEquals("Invitation with token " + token + " does not exist.", exception.getMessage());
        verify(invitationRepository, times(1)).findByInvitationToken(token);
        verify(invitationRepository, never()).save(any(InvitationEntity.class));
        verify(roomService, never()).findByInvitationToken(anyString());
        verify(roomService, never()).saveRoom(any(RoomEntity.class));
    }
}