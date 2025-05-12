package com.example.time.box.repository;


import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectSessionRepository extends JpaRepository<SubjectSession, Long> {

}
