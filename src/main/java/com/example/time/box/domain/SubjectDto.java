package com.example.time.box.domain;

import com.example.time.box.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SubjectDto {

    private Long id;
    private String name;
    private String description;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Long userId;

}