package com.example.time.box.security;

import com.example.time.box.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private UserEntity userEntity;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .hashedPassword("hashedPassword")
                .emailVerified(true)
                .build();

        customUserDetails = new CustomUserDetails(userEntity);
    }

    @Test
    void getAuthorities() {
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void getPassword() {
        assertEquals("hashedPassword", customUserDetails.getPassword());
    }

    @Test
    void getUsername() {
        assertEquals("John", customUserDetails.getUsername());
    }

    @Test
    void isAccountNonExpired() {
        assertTrue(customUserDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        assertTrue(customUserDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        assertTrue(customUserDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        assertTrue(customUserDetails.isEnabled());
    }
}