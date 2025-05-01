package com.example.time.box.repository;

import com.example.time.box.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    SubjectEntity save(SubjectEntity subjectEntity);
    void deleteById(Long id);
    List<SubjectEntity> findAll();
    List<SubjectEntity> findByUserId(Long userId);

}
