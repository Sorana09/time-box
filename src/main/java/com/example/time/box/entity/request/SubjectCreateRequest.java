package com.example.time.box.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SubjectCreateRequest {
    private Long userId;
    private String name;
    private String description;
}
