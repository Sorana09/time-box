package com.example.time.box.service;

import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.UserSignInRequest;
import com.example.time.box.entity.request.UserSignUpRequest;
import com.example.time.box.exception.*;
import com.example.time.box.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean verifyPassword(Long id, String password){
        Optional<UserEntity> user = userRepository.findById(id);
        if(password == null){
            throw new PasswordIsNullException();
        }
        return user.map(it-> passwordEncoder.matches(password,it.getHashedPassword())).orElse(false);
    }

    public boolean signUp(final UserSignUpRequest userSignUpRequest) throws Exception {
        if(userRepository.findByEmail(userSignUpRequest.getEmail()).isPresent()){
            throw new EmailAlreadyRegisteredException();
        }
        UserEntity userEntity = UserEntity.builder()
                .email(userSignUpRequest.getEmail())
                .hashedPassword(userSignUpRequest.getPassword())
                .firstName(userSignUpRequest.getFirstName())
                .lastName(userSignUpRequest.getLastName())
                .createdAt(OffsetDateTime.now())
                .build();

        return userEntity.getId() > 0;
    }

    public UserEntity signIn(final UserSignInRequest userSignInRequest) {
        if(userRepository.findByEmail(userSignInRequest.getEmail()).isEmpty()){
            throw new EmailisNotRegisteredException();
        }
        UserEntity userEntity = userRepository.findByEmail(userSignInRequest.getEmail()).get();

        if(!userEntity.getHashedPassword().equals(userSignInRequest.getPassword())){
            throw new IncorrectPasswordException();
        }

        return userEntity;

    }
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
