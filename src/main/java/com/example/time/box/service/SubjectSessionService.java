package com.example.time.box.service;


import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.request.SubjectSessionRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.SubjectRepository;
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
    private final SubjectRepository subjectRepository;

    public List<SubjectSession> getAllSubjectSessions() {
        return subjectSessionRepository.findAll();
    }


    public SubjectSession getSubjectSessionById(Long id) {
        return subjectSessionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public SubjectSession saveSubjectSession(SubjectSessionRequest  subjectSessionRequest) {
        return subjectSessionRepository.save(SubjectSession.builder()
                .subjectId(subjectSessionRequest.getSubjectId())
                .startTime(null)
                .endTime(null)
                .running(false)
                .build());
    }

    public void setStartTime(Long subjectId){
        SubjectEntity subjectEntity = subjectRepository.findById(subjectId).get();

        subjectEntity.setNumberOfSessions(subjectEntity.getNumberOfSessions() != null ? subjectEntity.getNumberOfSessions() + 1 :  0);
        subjectRepository.save(subjectEntity);

        SubjectSession subjectSession = new SubjectSession();

       subjectSession.setStartTime(OffsetDateTime.now());
        subjectSession.setEndTime(null);
        subjectSession.setRunning(true);

        subjectSession.setSubjectId(subjectId);

        subjectSessionRepository.save(subjectSession);
    }

    public void setEndTime(Long id){
        SubjectSession subjectSession = subjectSessionRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        subjectSession.setEndTime(OffsetDateTime.now());
        subjectSession.setRunning(false);

        subjectSession.setTimeAllotted(Duration.between(subjectSession.getStartTime(), subjectSession.getEndTime()).getSeconds());

        subjectSessionRepository.save(subjectSession);
    }

    public void pauseSession(Long id){
        SubjectSession subjectSession = subjectSessionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        subjectSession.setRunning(false);
        subjectSessionRepository.save(subjectSession);

    }
    public void restartSession(Long id){
        SubjectSession subjectSession = subjectSessionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        subjectSession.setRunning(true);
    }

    public Long durationForAnSessions(Long id){
        SubjectSession subjectSession =subjectSessionRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if(subjectSession.getStartTime() != null && subjectSession.getEndTime() != null){
            Long sessionDuration = Duration.between(subjectSession.getStartTime(), subjectSession.getEndTime()).getSeconds();
            subjectSession.setTimeAllotted((subjectSession.getTimeAllotted() !=null ? subjectSession.getTimeAllotted() : 0L) + sessionDuration);
            subjectSession.setStartTime(null);
            subjectSession.setEndTime(null);
        }
        return subjectSession.getTimeAllotted() != null ? subjectSession.getTimeAllotted() : 0L;
    }


    public void deleteSubjectSession(Long id) {
        subjectSessionRepository.deleteById(id);
    }

}
