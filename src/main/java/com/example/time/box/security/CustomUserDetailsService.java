package com.example.time.box.security;

import com.example.time.box.entity.UserEntity;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException());
        return new CustomUserDetails(user);
    }
}