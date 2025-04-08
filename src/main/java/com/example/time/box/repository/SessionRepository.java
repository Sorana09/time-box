package com.example.time.box.repository;

import com.example.time.box.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    List<SessionEntity> findByUserId(Long userId);

   SessionEntity save(SessionEntity sessionEntity);

    void deleteById(Long id);
    //void deleteBySessionKey(Long userId);

}
