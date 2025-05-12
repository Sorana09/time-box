package com.example.time.box.repository;

import com.example.time.box.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    List<SessionEntity> findByUserId(Long userId);

    Optional<SessionEntity> findBySessionKey(String sessionKey);

    void deleteById(Long id);

    void deleteBySessionKey(String sessionKey);

}
