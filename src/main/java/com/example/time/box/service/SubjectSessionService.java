package com.example.time.box.service;


import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.request.SubjectSessionRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.SubjectSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SubjectSessionService {

    private final SubjectSessionRepository subjectSessionRepository;

    public List<SubjectSession> getAllSubjectSessions() {
        return subjectSessionRepository.findAll();
    }


    public SubjectSession getSubjectSessionById(Long id) {
        return subjectSessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public SubjectSession saveSubjectSession(SubjectSessionRequest  subjectSessionRequest) {
        return SubjectSession.builder()
                .subjectId(subjectSessionRequest.getSubjectId())
                .startTime(null)
                .endTime(null)
                .running(false)
                .build();
    }

    public void setStartTime(Long id){
        SubjectSession subjectSession =subjectSessionRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        Integer subjectSessionCount = subjectSession.getNumberOfSessions();
        subjectSessionCount++;
        subjectSession.setNumberOfSessions(subjectSessionCount);

        subjectSession.setStartTime(OffsetDateTime.now());
        subjectSession.setEndTime(null);
        subjectSession.setRunning(true);

        subjectSessionRepository.save(subjectSession);
    }

    public void setEndTime(Long id){
        SubjectSession subjectSession =subjectSessionRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        subjectSession.setEndTime(OffsetDateTime.now());
        subjectSession.setRunning(false);

        subjectSessionRepository.save(subjectSession);
    }

    public Long durationForAnSessions(Long id){
        SubjectSession subjectSession =subjectSessionRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if(subjectSession.getStartTime() != null && subjectSession.getEndTime() != null){
            Long sessionDuration = Duration.between(subjectSession.getStartTime(), subjectSession.getEndTime()).getSeconds();
            subjectSession.setTimeAllotted((subjectSession.getTimeAllotted() !=null ? subjectSession.getTimeAllotted() : 0L) +sessionDuration);
            subjectSession.setStartTime(null);
            subjectSession.setEndTime(null);
        }
        return subjectSession.getTimeAllotted() != null ? subjectSession.getTimeAllotted() : 0L;
    }

    public void deleteSubjectSession(Long id) {
        subjectSessionRepository.deleteById(id);
    }

}
