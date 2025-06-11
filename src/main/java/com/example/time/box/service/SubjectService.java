package com.example.time.box.service;


import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.request.SubjectCreateRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectSessionService subjectSessionService;
    private final UserService userService;

    public Integer getNumberOfSessionsForAnSubject(Long id){
        return subjectSessionService.getAllSubjectSessions().stream()
                .filter(it -> it.getSubjectId().equals(id))
                .toList().size();

    }

    public Long timeAllotedForAnSubject(Long id){
        List<SubjectSession> subjectSessions = subjectSessionService.getAllSubjectSessions().stream()
                .filter(it -> it.getSubjectId().equals(id))
                .toList();
        Long totalTimeAllotted = findById(id).getTimeAllotted();
        for(SubjectSession subjectSession : subjectSessions){
            totalTimeAllotted += subjectSession.getTimeAllotted();
        }

        return totalTimeAllotted;
    }

    public List<SubjectEntity> findAll() {
        return subjectRepository.findAll();
    }

    public List<SubjectEntity> findAllByUserId(Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new EntityNotFoundException();
        }
        return subjectRepository.findByUserId(userId);
    }

    public SubjectEntity findById(Long id) {
        return subjectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
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
        subject.setUserId(subjectCreateRequest.getUserId());

        return subjectRepository.save(subject);
    }



    public void deleteById(Long id) {
        subjectRepository.deleteById(id);
    }


}
