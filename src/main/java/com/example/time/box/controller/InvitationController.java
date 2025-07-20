package com.example.time.box.controller;

import com.example.time.box.entity.InvitationEntity;
import com.example.time.box.entity.RoomEntity;
import com.example.time.box.entity.request.CreateInvitationRequest;
import com.example.time.box.service.InvitationService;
import com.example.time.box.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/invitation")
public class InvitationController {
    private final InvitationService invitationService;
    private final RoomService roomService;

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<InvitationEntity> createInvitation(@RequestBody CreateInvitationRequest request) {
        InvitationEntity invitation = invitationService.createInvitation(request);
        return ResponseEntity.ok(invitation);
    }

    @PostMapping(value = "/{token}/accept", consumes = {"application/json", "application/x-www-form-urlencoded", "text/plain", "*/*"})
    public ResponseEntity<Map<String, Object>> acceptInvitation(
            @PathVariable String token,
            @RequestBody String userEmail) {

        InvitationEntity invitation = invitationService.acceptInvitation(token, userEmail);
        Optional<RoomEntity> roomOpt = roomService.findByInvitationToken(token);

        if (roomOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RoomEntity room = roomOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("invitation", invitation);
        response.put("room", room);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{token}")
    public ResponseEntity<InvitationEntity> getInvitation(@PathVariable String token) {
        Optional<InvitationEntity> invitationOpt = invitationService.findByToken(token);

        if (invitationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(invitationOpt.get());
    }
}
