package com.example.time.box.repository;


import com.example.time.box.entity.SubjectSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectSessionRepository extends JpaRepository<SubjectSession, Long> {

}
