package com.example.time.box.service;

import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.LoginRequest;
import com.example.time.box.exception.EmailNotVerifiedException;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.exception.IncorrectPasswordException;
import com.example.time.box.exception.PasswordIsNullException;
import com.example.time.box.exception.TooManySessionsException;
import com.example.time.box.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserService userService;

    public List<SessionEntity> find(Long userId, Boolean active) {
        List<SessionEntity> sessionEntities = sessionRepository.findByUserId(userId);
        if (!active) {
            return sessionEntities;
        }
        if (active) {
            return sessionEntities.stream().filter(sessionEntity1 -> !isExpired(sessionEntity1)).collect(Collectors.toList());

        }
        return sessionEntities.stream().filter(SessionService::isExpired).collect(Collectors.toList());
    }

    public static Boolean isExpired(SessionEntity sessionEntity) {
        return sessionEntity.getExpiredAt().isBefore(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public List<SessionEntity> getActiveSessions() {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        return sessionEntities.stream()
                .filter(sessionEntity -> !isExpired(sessionEntity))
                .toList();
    }

    public Optional<SessionEntity> createSession(LoginRequest loginRequest) {
        Optional<UserEntity> userEntity = userService.findByEmail(loginRequest.getEmail());

        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException();
        }

        UserEntity user = userEntity.get();

        if (loginRequest.getPassword() == null) {
            throw new PasswordIsNullException();
        }

        if (!userService.verifyPassword(user.getId(), loginRequest.getPassword())) {
            throw new IncorrectPasswordException();
        }

        // Check if the user's email is verified
        if (user.getEmailVerified() == null || !user.getEmailVerified()) {
            throw new EmailNotVerifiedException();
        }

        if (find(user.getId(), true).size() >= 3) {
            throw new TooManySessionsException();
        }

        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setExpiredAt(OffsetDateTime.now().plusDays(30));
        UUID key = UUID.randomUUID();
        sessionEntity.setSessionKey(String.valueOf(key));
        sessionEntity.setUserId(user.getId());

        sessionRepository.save(sessionEntity);
        return Optional.of(sessionEntity);
    }

    public Optional<SessionEntity> getSessionBySessionKey(String sessionKey) {
        return sessionRepository.findBySessionKey(sessionKey);
    }

    public void deleteSessionById(Long id) {
        sessionRepository.deleteById(id);
    }

    public void deleteSessionBySessionKey(String sessionKey) {
        sessionRepository.deleteBySessionKey(sessionKey);
    }
}
