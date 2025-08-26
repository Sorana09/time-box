package com.example.time.box.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AchievementCreateRequest {
    private String name;
    private String description;
    private Long subjectId;
    private Long userId;
    private java.time.OffsetDateTime deadline;
}