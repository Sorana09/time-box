package com.example.time.box.service;

import com.example.time.box.domain.SessionDto;
import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.GoogleRequest;
import com.example.time.box.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GoogleService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionEntityRepository;

    public SessionDto googleLogin(GoogleRequest googleRequest) {
        String email = googleRequest.getEmail();
        String firstName = Optional.ofNullable(googleRequest.getFirstName()).orElse("");
        String lastName = Optional.ofNullable(googleRequest.getLastName()).orElse("");

        UserEntity user = userService.findByEmail(email)
                .orElseGet(() -> {
                    String randomPassword = UUID.randomUUID().toString();
                    UserEntity created = UserEntity.builder()
                            .email(email)
                            .firstName(firstName)
                            .lastName(lastName)
                            .hashedPassword(passwordEncoder.encode(randomPassword))
                            .createdAt(OffsetDateTime.now())
                            .emailVerified(true)
                            .build();
                    userService.save(created);
                    return created;
                });

        List<SessionEntity> active = userService.getAllSubjectSesionsForAnUser(user.getId()) != null
                ? sessionEntityRepository.findByUserId(user.getId()).stream().filter(s -> !SessionService.isExpired(s)).toList()
                : sessionEntityRepository.findByUserId(user.getId());

        if (active.stream().filter(s -> !SessionService.isExpired(s)).count() >= 3) {
            return null;
        }
        SessionEntity session = new SessionEntity();
        session.setUserId(user.getId());
        session.setSessionKey(UUID.randomUUID().toString());
        session.setExpiredAt(OffsetDateTime.now().plusDays(30));
        sessionEntityRepository.save(session);
        return SessionDto.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .sessionKey(session.getSessionKey())
                .expiredAt(session.getExpiredAt())
                .build();
    }

}
