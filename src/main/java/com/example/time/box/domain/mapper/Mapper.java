package com.example.time.box.domain.mapper;

import com.example.time.box.domain.SessionDto;
import com.example.time.box.domain.SubjectDto;
import com.example.time.box.domain.UserDto;
import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public static SessionDto mapper(SessionEntity sessionEntity) {
        return SessionDto.builder()
                .id(sessionEntity.getId())
                .userId(sessionEntity.getUserId())
                .expiredAt(sessionEntity.getExpiredAt())
                .sessionKey(sessionEntity.getSessionKey())
                .build();
    }

    public static UserDto mapper(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .firstName(userEntity.getFirstName())
                .hashedPassword(userEntity.getHashedPassword())
                .lastName(userEntity.getLastName())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    public static SubjectDto mapper(SubjectEntity subjectEntity) {
        return SubjectDto.builder()
                .id(subjectEntity.getId())
                .name(subjectEntity.getName())
                .description(subjectEntity.getDescription())
                .startTime(subjectEntity.getStartTime())
                .endTime(subjectEntity.getEndTime())
                .userId(subjectEntity.getUser().getId())
                .build();
    }

}
