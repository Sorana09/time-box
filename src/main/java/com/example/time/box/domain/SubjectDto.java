package com.example.time.box.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SubjectDto {

    private Long id;
    private String name;
    private String description;
    private Long userId;
    private Long timeAllotted;
    private Integer numberOfSessions;


}