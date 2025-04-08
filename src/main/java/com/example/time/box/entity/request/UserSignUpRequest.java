package com.example.time.box.entity.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserSignUpRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

}