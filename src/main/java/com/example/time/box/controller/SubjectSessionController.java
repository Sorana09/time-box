package com.example.time.box.controller;

import com.example.time.box.domain.SubjectSessionDto;
import com.example.time.box.domain.mapper.Mapper;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.request.SubjectCreateRequest;
import com.example.time.box.entity.request.SubjectSessionRequest;
import com.example.time.box.service.SubjectSessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.time.box.domain.mapper.Mapper.mapper;

@RestController
@RequestMapping("/subject-session")
@AllArgsConstructor
public class SubjectSessionController {
    private final SubjectSessionService subjectSessionService;

    @GetMapping
    public ResponseEntity<List<SubjectSessionDto>> getAllSubjectSessions(){
        return ResponseEntity.ok().body(subjectSessionService.getAllSubjectSessions().stream()
                .map(Mapper::mapper)
                .toList());
    }

    @PostMapping
    public ResponseEntity<SubjectSessionDto> createSubjectSession(@RequestBody SubjectSessionRequest subjectSessionRequest){
        return ResponseEntity.ok().body(mapper(subjectSessionService.saveSubjectSession(subjectSessionRequest)));
    }

    @PutMapping("/{id}/pause")
    public void pauseSession(@PathVariable(name = "id") Long id){
        subjectSessionService.pauseSession(id);
    }

    @PutMapping("/{id}/restart")
    public void restartSession(@PathVariable(name = "id") Long id){
        subjectSessionService.restartSession(id);
    }

    @PutMapping("/{id}/start")
    public void setStartTime(@PathVariable(name = "id") Long id){
        subjectSessionService.setStartTime(id);
    }

    @PutMapping("/{id}/end")
    public void setEndTime(@PathVariable(name ="id") Long id){
        subjectSessionService.setEndTime(id);
    }

    @GetMapping("/duration/{id}")
    public ResponseEntity<Long> getDurationTime(@PathVariable(name ="id") Long id) {
        return ResponseEntity.ok().body(subjectSessionService.durationForAnSessions(id));
    }

    @DeleteMapping("/{id}")
    public void deleteSubjectSession(@PathVariable Long id){
        subjectSessionService.deleteSubjectSession(id);
    }

}
