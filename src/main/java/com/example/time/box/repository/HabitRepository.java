package com.example.time.box.repository;

import com.example.time.box.entity.HabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<HabitEntity,Long> {
    List<HabitEntity> findHabitEntityByUserId(Long userId);

    List<HabitEntity> findByUserIdAndActiveTrue(Long userId);
}
