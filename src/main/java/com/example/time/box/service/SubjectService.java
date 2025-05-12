package com.example.time.box.service;


import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.request.SubjectCreateRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO : caltulate the duration of the all sessions for a subject

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
        subject.setUserId(subjectCreateRequest.getUserId());

        return subjectRepository.save(subject);
    }



    public void deleteById(Long id) {
        subjectRepository.deleteById(id);
    }


}
