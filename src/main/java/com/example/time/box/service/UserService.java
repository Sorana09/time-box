package com.example.time.box.service;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.UserSignInRequest;
import com.example.time.box.entity.request.UserSignUpRequest;
import com.example.time.box.exception.*;
import com.example.time.box.repository.SubjectRepository;
import com.example.time.box.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Lazy
    private final SubjectService subjectService;
    private final SubjectSessionService subjectSessionService;
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

    public List<SubjectSession> getAllSubjectSesionsForAnUser(Long userId){
        List<SubjectEntity> subjectEntities = subjectService.findAllByUserId(userId);
        return subjectEntities.stream()
                .map(it -> subjectSessionService.getAllSubjectSessions().stream()
                        .filter(subjectSession -> subjectSession.getSubjectId().equals(it.getId()))
                        .toList())
                .flatMap(List::stream)
                .toList();
    }

    public void save(UserEntity userEntity){
        userRepository.save(userEntity);
    }

    @Transactional
    public Integer daysStreak(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        List<SubjectSession> subjectSessions = getAllSubjectSesionsForAnUser(userId);
        if (subjectSessions.isEmpty()) {
            user.setDaysStreak(0);
            userRepository.save(user);
            return 0;
        }
        List<LocalDate> studyDates = subjectSessions.stream()
                .map(session -> session.getStartTime().toLocalDate())
                .distinct()
                .sorted()
                .toList();

        int streak = 1;
        LocalDate today = LocalDate.now();
        LocalDate lastDate = studyDates.get(studyDates.size() - 1);

        if (ChronoUnit.DAYS.between(lastDate, today) > 1) {
            user.setDaysStreak(0);
            userRepository.save(user);
            return 0;
        }

        for (int i = studyDates.size() - 1; i > 0; i--) {
            if (ChronoUnit.DAYS.between(studyDates.get(i-1), studyDates.get(i)) == 1) {
                streak++;
            } else {
                break;
            }
        }

        user.setDaysStreak(streak);
        userRepository.save(user);

        return streak;
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

        List<SubjectEntity> subjects = subjectService.findAllByUserId(id);
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
