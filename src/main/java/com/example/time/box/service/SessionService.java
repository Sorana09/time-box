package com.example.time.box.service;

import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.LoginRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.exception.PasswordIsNullException;
import com.example.time.box.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserService userService;

    // TODO : Add methods to interact with the session repository
    public Boolean isExpired(SessionEntity sessionEntity) {
        return sessionEntity.getExpiredAt().isBefore(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public Optional<SessionEntity> createSession(LoginRequest loginRequest) {
        Optional<UserEntity> userEntity = userService.findByEmail(loginRequest.getEmail());

        if(userEntity.isEmpty()){
            throw new EntityNotFoundException();
        }

        UserEntity user = userEntity.get();

        if(loginRequest.getHashedPassword() == null) {
            throw new PasswordIsNullException();
        }

        SessionEntity sessionEntity = new SessionEntity();
        sessionRepository.save(sessionEntity);
        return Optional.of(sessionEntity);
    }

    public void deleteSessionById(Long id) {
        sessionRepository.deleteById(id);
    }
    public void deleteSessionBySessionKey(Long userId) {
        sessionRepository.deleteBySessionKey(userId);
    }
}
