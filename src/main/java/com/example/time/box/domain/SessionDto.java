package com.example.time.box.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionDto {
    private Integer id;
    private String sessionKey;
    private OffsetDateTime expiredAt;
    private Long userId;
}