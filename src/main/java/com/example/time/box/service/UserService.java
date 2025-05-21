package com.example.time.box.service;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.UserSignInRequest;
import com.example.time.box.entity.request.UserSignUpRequest;
import com.example.time.box.exception.EmailAlreadyRegisteredException;
import com.example.time.box.exception.EmailisNotRegisteredException;
import com.example.time.box.exception.IncorrectPasswordException;
import com.example.time.box.exception.PasswordIsNullException;
import com.example.time.box.repository.SubjectRepository;
import com.example.time.box.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean verifyPassword(Long id, String password) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (password == null) {
            throw new PasswordIsNullException();
        }
        return user.map(it -> passwordEncoder.matches(password, it.getHashedPassword())).orElse(false);
    }

    public UserEntity signUp(final UserSignUpRequest userSignUpRequest) {
        if (userRepository.findByEmail(userSignUpRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }
        UserEntity userEntity = UserEntity.builder()
                .email(userSignUpRequest.getEmail())
                .hashedPassword(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .firstName(userSignUpRequest.getFirstName())
                .lastName(userSignUpRequest.getLastName())
                .createdAt(OffsetDateTime.now())
                .build();

        return userRepository.save(userEntity);
    }

    public UserEntity signIn(final UserSignInRequest userSignInRequest) {
        if (userRepository.findByEmail(userSignInRequest.getEmail()).isEmpty()) {
            throw new EmailisNotRegisteredException();
        }
        UserEntity userEntity = userRepository.findByEmail(userSignInRequest.getEmail()).get();

        if (!userEntity.getHashedPassword().equals(userSignInRequest.getPassword())) {
            throw new IncorrectPasswordException();
        }

        return userEntity;

    }

    public Long timeStudied(Long id){
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<SubjectEntity> subjects = subjectRepository.findAllByUserId(id);
        subjects.forEach(it -> {
            userEntity.setTimeStudied(userEntity.getTimeStudied() + it.getTimeAllotted());
        });
        userRepository.save(userEntity);
        return userEntity.getTimeStudied();
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
