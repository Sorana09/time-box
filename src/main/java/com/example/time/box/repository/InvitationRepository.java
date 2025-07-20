package com.example.time.box.repository;

import com.example.time.box.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity,Long> {
    Optional<InvitationEntity> findByInvitationToken(String invitationToken);
}
