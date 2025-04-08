package com.example.time.box.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
