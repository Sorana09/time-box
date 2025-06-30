package com.example.time.box.service;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.UserEntity;
import com.example.time.box.entity.request.UserSignUpRequest;
import com.example.time.box.exception.*;
import com.example.time.box.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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

        UserEntity userEntity = UserEntity.builder()
                .email(userSignUpRequest.getEmail())
                .hashedPassword(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .firstName(userSignUpRequest.getFirstName())
                .lastName(userSignUpRequest.getLastName())
                .createdAt(OffsetDateTime.now())
                .emailVerified(false)
                .build();

        return userRepository.save(userEntity);
    }

    public List<SubjectSession> getAllSubjectSesionsForAnUser(Long userId) {
        List<SubjectEntity> subjectEntities = subjectService.findAllByUserId(userId);
        return subjectEntities.stream()
                .map(it -> subjectSessionService.getAllSubjectSessions().stream()
                        .filter(subjectSession -> subjectSession.getSubjectId().equals(it.getId()))
                        .toList())
                .flatMap(List::stream)
                .toList();
    }

    public void save(UserEntity userEntity) {
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
            if (ChronoUnit.DAYS.between(studyDates.get(i - 1), studyDates.get(i)) == 1) {
                streak++;
            } else {
                break;
            }
        }

        user.setDaysStreak(streak);
        userRepository.save(user);

        return streak;
    }

    public Integer setWeeklyGoal(Long id, Integer weeklyGoal) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        userEntity.setWeeklyGoal(weeklyGoal);
        userRepository.save(userEntity);
        return weeklyGoal;
    }

    public void setDailyStudyTime(Long id, Integer dailyStudyTime) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<SubjectSession> subjectSessions = getAllSubjectSesionsForAnUser(id);
        subjectSessions.stream()
                .filter(it -> it.getStartTime().getDayOfYear() == OffsetDateTime.now().getDayOfYear());
        dailyStudyTime = (int) subjectSessions.stream()
                .map(it -> it.getTimeAllotted())
                .count();
        userEntity.setDailyStudyTime(dailyStudyTime);
        userRepository.save(userEntity);
    }

    public Integer calculateAvgSession(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<SubjectSession> subjectSessions = getAllSubjectSesionsForAnUser(id);

        if (subjectSessions.isEmpty()) {
            userEntity.setAvgSession(0);
            userRepository.save(userEntity);
            return 0;
        }

        Long totalTime = subjectSessions.stream()
                .map(SubjectSession::getTimeAllotted)
                .filter(Objects::nonNull)
                .reduce(0L, Long::sum);

        Integer avgTime = (int) (totalTime / subjectSessions.size());

        userEntity.setAvgSession(avgTime);
        userRepository.save(userEntity);

        return avgTime;
    }

    public Integer calculateTodaySessions(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<SubjectSession> subjectSessions = getAllSubjectSesionsForAnUser(id);

        if (subjectSessions.isEmpty()) {
            userEntity.setTodaySessions(0);
            userRepository.save(userEntity);
            return 0;
        }

        int todaySessionsCount = (int) subjectSessions.stream()
                .filter(session -> session.getStartTime() != null &&
                        session.getStartTime().toLocalDate().equals(LocalDate.now()))
                .count();

        userEntity.setTodaySessions(todaySessionsCount);
        userRepository.save(userEntity);

        return todaySessionsCount;
    }

    public String calculateMostProductiveSubject(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<SubjectEntity> subjects = subjectService.findAllByUserId(id);

        if (subjects.isEmpty()) {
            userEntity.setMostProductiveSubject("None");
            userRepository.save(userEntity);
            return "None";
        }

        SubjectEntity mostProductiveSubject = subjects.stream()
                .filter(subject -> subject.getTimeAllotted() != null)
                .max((s1, s2) -> s1.getTimeAllotted().compareTo(s2.getTimeAllotted()))
                .orElse(null);

        if (mostProductiveSubject == null) {
            userEntity.setMostProductiveSubject("None");
            userRepository.save(userEntity);
            return "None";
        }

        String mostProductiveSubjectName = mostProductiveSubject.getName();
        userEntity.setMostProductiveSubject(mostProductiveSubjectName);
        userRepository.save(userEntity);

        return mostProductiveSubjectName;
    }

    public Integer calculateLongestSession(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<SubjectSession> subjectSessions = getAllSubjectSesionsForAnUser(id);

        if (subjectSessions.isEmpty()) {
            userEntity.setLongestSession(0);
            userRepository.save(userEntity);
            return 0;
        }

        SubjectSession longestSession = subjectSessions.stream()
                .filter(session -> session.getTimeAllotted() != null)
                .max((s1, s2) -> s1.getTimeAllotted().compareTo(s2.getTimeAllotted()))
                .orElse(null);

        if (longestSession == null) {
            userEntity.setLongestSession(0);
            userRepository.save(userEntity);
            return 0;
        }

        Integer longestSessionDuration = longestSession.getTimeAllotted().intValue();
        userEntity.setLongestSession(longestSessionDuration);
        userRepository.save(userEntity);

        return longestSessionDuration;
    }

    public Integer getDailyStudyTime(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (userEntity.getDailyStudyTime() == null) {
            setDailyStudyTime(id, 0);
        }

        return userEntity.getDailyStudyTime();
    }


    public Long timeStudied(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

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
