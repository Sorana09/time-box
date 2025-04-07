package com.example.time.box.service;

import com.example.time.box.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    // TODO : Add methods to interact with the session repository
}
