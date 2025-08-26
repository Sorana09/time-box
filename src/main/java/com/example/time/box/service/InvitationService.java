package com.example.time.box.service;

import com.example.time.box.entity.InvitationEntity;
import com.example.time.box.entity.RoomEntity;
import com.example.time.box.entity.request.CreateInvitationRequest;
import com.example.time.box.repository.InvitationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final RoomService roomService;

    public InvitationEntity createInvitation(CreateInvitationRequest request) {
        InvitationEntity invitation = InvitationEntity.builder()
                .invitedEmail(request.getInvitedEmail())
                .description(request.getDescription())
                .invitedBy(request.getInvitedBy())
                .createdAt(OffsetDateTime.now())
                .accepted(false)
                .invitationToken(UUID.randomUUID().toString())
                .build();

        return invitationRepository.save(invitation);
    }

    public Optional<InvitationEntity> findByToken(String token) {
        return invitationRepository.findByInvitationToken(token);
    }

    public InvitationEntity acceptInvitation(String token, String userEmail) {
        Optional<InvitationEntity> invitationOpt = findByToken(token);

        if (invitationOpt.isEmpty()) {
            throw new IllegalArgumentException("Invitation with token " + token + " does not exist.");
        }

        InvitationEntity invitation = invitationOpt.get();

        invitation.setAccepted(true);
        invitationRepository.save(invitation);

        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(token);
        RoomEntity room;

        if (roomOpt.isPresent()) {
            room = roomOpt.get();
        } else {
            room = new RoomEntity();
            room.setInvitationToken(token);
            room.setDescription(invitation.getDescription());
            room.setCreatedBy(invitation.getInvitedBy());
            room.setCreatedAt(OffsetDateTime.now());
            room.setIsActive(true);
            room.setChatEnabled(true);
            room.setParticipants(new ArrayList<>());
        }

        if (!room.getParticipants().contains(userEmail)) {
            room.getParticipants().add(userEmail);
        }

        roomService.saveRoom(room);

        return invitation;
    }
}
