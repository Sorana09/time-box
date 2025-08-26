package com.example.time.box.controller;

import com.example.time.box.domain.SessionDto;
import com.example.time.box.entity.request.GoogleRequest;
import com.example.time.box.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleService googleService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/google")
    public ResponseEntity<SessionDto> googleLogin(@RequestBody GoogleRequest request) {
        return ResponseEntity.ok().body(googleService.googleLogin(request));
    }


}
