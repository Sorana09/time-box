package com.example.time.box.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserSignInRequest {
    private String email;
    private String password;
}
