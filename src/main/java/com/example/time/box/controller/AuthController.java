package com.example.time.box.controller;

import com.example.time.box.domain.SessionDto;
import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.repository.SessionRepository;
import com.example.time.box.service.SessionService;
import com.example.time.box.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/google")
    public ResponseEntity<SessionDto> googleLogin(@RequestBody GoogleLoginRequest request) {
        String email = request.getEmail();
        String firstName = Optional.ofNullable(request.getFirstName()).orElse("");
        String lastName = Optional.ofNullable(request.getLastName()).orElse("");

        UserEntity user = userService.findByEmail(email)
                .orElseGet(() -> {
                    String randomPassword = UUID.randomUUID().toString();
                    UserEntity created = UserEntity.builder()
                            .email(email)
                            .firstName(firstName)
                            .lastName(lastName)
                            .hashedPassword(passwordEncoder.encode(randomPassword))
                            .emailVerified(true)
                            .createdAt(OffsetDateTime.now())
                            .build();
                    userService.save(created);
                    return created;
                });

        // limit concurrent sessions
        List<SessionEntity> active = userService.getAllSubjectSesionsForAnUser(user.getId()) != null
                ? sessionRepository.findByUserId(user.getId()).stream().filter(s -> !SessionService.isExpired(s)).toList()
                : sessionRepository.findByUserId(user.getId());
        if (active.stream().filter(s -> !SessionService.isExpired(s)).count() >= 3) {
            return ResponseEntity.status(429).build();
        }

        SessionEntity session = new SessionEntity();
        session.setUserId(user.getId());
        session.setSessionKey(UUID.randomUUID().toString());
        session.setExpiredAt(OffsetDateTime.now().plusDays(30));
        sessionRepository.save(session);
        // re-fetch to ensure ID populated
        SessionEntity saved = sessionRepository.findBySessionKey(session.getSessionKey()).orElse(session);
        SessionDto dto = SessionDto.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .sessionKey(saved.getSessionKey())
                .expiredAt(saved.getExpiredAt())
                .build();

        return ResponseEntity.ok(dto);
    }

    @Data
    static class GoogleLoginRequest {
        private String email;
        private String firstName;
        private String lastName;
    }
}
