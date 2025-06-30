package com.example.time.box.domain.mapper;

import com.example.time.box.domain.AchievementDto;
import com.example.time.box.domain.SessionDto;
import com.example.time.box.domain.SubjectDto;
import com.example.time.box.domain.SubjectSessionDto;
import com.example.time.box.domain.UserDto;
import com.example.time.box.entity.AchievementEntity;
import com.example.time.box.entity.SessionEntity;
import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public static SessionDto mapper(SessionEntity sessionEntity) {
        return SessionDto.builder()
                .id(sessionEntity.getId())
                .userId(sessionEntity.getUserId())
                .expiredAt(sessionEntity.getExpiredAt())
                .sessionKey(sessionEntity.getSessionKey())
                .build();
    }

    public static UserDto mapper(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .firstName(userEntity.getFirstName())
                .hashedPassword(userEntity.getHashedPassword())
                .lastName(userEntity.getLastName())
                .updatedAt(userEntity.getUpdatedAt())
                .timeStudied(userEntity.getTimeStudied())
                .dailyStudyTime(userEntity.getDailyStudyTime())
                .daysStreak(userEntity.getDaysStreak())
                .longestSession(userEntity.getLongestSession())
                .mostProductiveSubject(userEntity.getMostProductiveSubject())
                .todaySessions(userEntity.getTodaySessions())
                .weeklyGoal(userEntity.getWeeklyGoal())
                .achievementsCount(userEntity.getAchievements() != null ?
                        (int) userEntity.getAchievements().stream().filter(a -> a.getCompleted() != null && a.getCompleted()).count() : 0)
                .emailVerified(userEntity.getEmailVerified())
                .build();
    }

    public static SubjectDto mapper(SubjectEntity subjectEntity) {
        return SubjectDto.builder()
                .id(subjectEntity.getId())
                .name(subjectEntity.getName())
                .description(subjectEntity.getDescription())
                .userId(subjectEntity.getUserId())
                .timeAllotted(subjectEntity.getTimeAllotted())
                .numberOfSessions(subjectEntity.getNumberOfSessions())
                .build();
    }

    public static SubjectSessionDto mapper(SubjectSession subjectSessions) {
        return SubjectSessionDto.builder()
                .id(subjectSessions.getId())
                .subjectId(subjectSessions.getSubjectId())
                .endTime(subjectSessions.getEndTime())
                .startTime(subjectSessions.getStartTime())
                .timeAllotted(subjectSessions.getTimeAllotted())
                .running(subjectSessions.getRunning())
                .build();
    }

    public static AchievementDto mapper(AchievementEntity achievementEntity) {
        return AchievementDto.builder()
                .id(achievementEntity.getId())
                .name(achievementEntity.getName())
                .description(achievementEntity.getDescription())
                .criteria(achievementEntity.getCriteria())
                .completed(achievementEntity.getCompleted())
                .userId(achievementEntity.getUserId())
                .build();
    }

}
