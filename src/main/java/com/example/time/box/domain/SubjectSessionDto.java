package com.example.time.box.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubjectSessionDto {
    private Long id;
    private Long subjectId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Boolean running;
    private Long timeAllotted;
}
