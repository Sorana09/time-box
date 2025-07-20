package com.example.time.box.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRoomRequest {
    private String createdBy;
    private String description;
}
