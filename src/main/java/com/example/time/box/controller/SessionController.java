package com.example.time.box.controller;

import com.example.time.box.domain.SessionDto;
import com.example.time.box.entity.request.LoginRequest;
import com.example.time.box.service.SessionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.time.box.domain.mapper.Mapper.mapper;

@RequestMapping("/sessions")
@RestController
@AllArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("{key}")
    public ResponseEntity<SessionDto> getSession(@PathVariable(name = "key") String key) {
        return sessionService.getSessionBySessionKey(key)
                .map(sessionEntity -> ResponseEntity.ok(mapper(sessionEntity)))
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<SessionDto> createSession(@RequestBody LoginRequest loginRequest) {
        return sessionService.createSession(loginRequest)
                .map(sessionEntity -> ResponseEntity.ok(mapper(sessionEntity)))
                .orElse(ResponseEntity.badRequest().build());
    }


    @DeleteMapping
    public ResponseEntity<Boolean> deleteSession(@RequestParam(name = "key") String key) {
        sessionService.deleteSessionBySessionKey(key);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SessionDto>> getActiveSessions() {
        return ResponseEntity.ok(sessionService.getActiveSessions()
                .stream().
                map(sessionEntity -> mapper(sessionEntity))
                .toList());
    }

}
