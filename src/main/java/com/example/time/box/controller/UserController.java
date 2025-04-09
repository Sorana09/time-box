package com.example.time.box.controller;

import com.example.time.box.domain.UserDto;
import com.example.time.box.domain.mapper.Mapper;
import com.example.time.box.entity.request.UserSignUpRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.time.box.service.UserService;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream().map(it -> Mapper.mapper(it)).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(Long id){
        UserDto user = Mapper.mapper(userService.getUserById(id));
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public Boolean signUp(UserSignUpRequest userSignUpRequest){
        return userService.signUp(userSignUpRequest);
    }

    @DeleteMapping
    public void deleteUser(Long id){
        userService.deleteUserById(id);
    }

}
