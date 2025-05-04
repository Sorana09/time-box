package com.example.time.box.service;


import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.request.SubjectCreateRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserService userService;


    public List<SubjectEntity> findAll() {
        return subjectRepository.findAll();
    }

    public List<SubjectEntity> findByUserId(Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new EntityNotFoundException();
        }
        return subjectRepository.findByUserId(userId);
    }


    public SubjectEntity save(SubjectCreateRequest subjectCreateRequest) {
        SubjectEntity subject = new SubjectEntity();
        subject.setName(subjectCreateRequest.getName());
        subject.setDescription(subjectCreateRequest.getDescription());
        subject.setStartTime(null);
        subject.setEndTime(null);
        subject.setRunning(false);
        subject.setUserId(subjectCreateRequest.getUserId());

        return subjectRepository.save(subject);
    }


    public void startSubjectTimer(Long id){
        SubjectEntity subject = subjectRepository.findById(id).orElseThrow(()-> new EntityNotFoundException());

        subject.setStartTime(OffsetDateTime.now());
        subject.setEndTime(null);
        subject.setRunning(true);
        Integer numberOfSessions = subject.getNumberOfSessions();
        numberOfSessions++;
        subject.setNumberOfSessions(numberOfSessions);
        subjectRepository.save(subject);
    }

    public void stopSubjectTimer(Long id){
        SubjectEntity subject = subjectRepository.findById(id).orElseThrow(()-> new EntityNotFoundException());

        subject.setEndTime(OffsetDateTime.now());
        subject.setRunning(false);
        subjectRepository.save(subject);
    }

    public Long durationForAnSubject(Long id) {
        SubjectEntity subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        if (subject.getStartTime() != null && subject.getEndTime() != null) {
            Long sessionDuration = Duration.between(subject.getStartTime(), subject.getEndTime()).getSeconds();
            subject.setTimeAllotted((subject.getTimeAllotted() != null ? subject.getTimeAllotted() :0L) + sessionDuration);
            subject.setStartTime(null);
            subject.setEndTime(null);
            subjectRepository.save(subject);
        }

        return subject.getTimeAllotted() != null ? subject.getTimeAllotted() : 0L;
    }

    public void deleteById(Long id) {
        subjectRepository.deleteById(id);
    }


}
