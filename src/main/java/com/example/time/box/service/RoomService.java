package com.example.time.box.service;

import com.example.time.box.entity.RoomEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.CreateRoomRequest;
import com.example.time.box.repository.RoomRepository;
import com.example.time.box.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;

    public RoomEntity createRoom(CreateRoomRequest request) {
        RoomEntity roomEntity = new RoomEntity();
        UUID invitationToken = UUID.randomUUID();
        roomEntity.setInvitationToken(invitationToken.toString());
        roomEntity.setDescription(request.getDescription());
        roomEntity.setCreatedAt(OffsetDateTime.now());
        roomEntity.setIsActive(true);
        roomEntity.setChatEnabled(true);
        roomEntity.setParticipants(new ArrayList<>());

        Optional<UserEntity> user = userService.findByEmail(request.getCreatedBy());
        if(user.isEmpty()) {
                throw new IllegalArgumentException("User with email " + request.getCreatedBy() + " does not exist.");
        }
        UserEntity userEntity = user.get();
        roomEntity.setCreatedBy(userEntity.getEmail());

        roomEntity.getParticipants().add(userEntity.getEmail());

        roomRepository.save(roomEntity);
        return roomEntity;
    }

    public Optional<RoomEntity> findByInvitationToken(String token) {
        return roomRepository.findByInvitationToken(token);
    }

    public RoomEntity saveRoom(RoomEntity room) {
        return roomRepository.save(room);
    }

    public RoomEntity addParticipant(String token, String userEmail) {
        Optional<RoomEntity> roomOpt = findByInvitationToken(token);

        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room with invitation token " + token + " does not exist.");
        }

        RoomEntity room = roomOpt.get();

        if (!room.getParticipants().contains(userEmail)) {
            room.getParticipants().add(userEmail);
            roomRepository.save(room);
        }

        return room;
    }
}
