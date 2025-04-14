package com.example.time.box.repository;

import com.example.time.box.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAll();
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long id);

    UserEntity save(UserEntity userEntity);

    void deleteById(Long id);

}
