package com.example.time.box.controller;

import com.example.time.box.domain.SubjectDto;
import com.example.time.box.domain.mapper.Mapper;
import com.example.time.box.entity.request.SubjectCreateRequest;
import com.example.time.box.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.time.box.domain.mapper.Mapper.mapper;

@RequestMapping("/subjects")
@RestController
@AllArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAllSubjects() {
        return ResponseEntity.ok().body(subjectService.findAll().stream()
                .map(Mapper::mapper)
                .toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<SubjectDto>> getAllSubjectByUserId(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(subjectService.findAllByUserId(userId).stream()
                .map(Mapper::mapper)
                .toList());
    }

    @PutMapping("/{id}/description")
    public ResponseEntity<String> changeDescription(@PathVariable(name = "id") Long id, @RequestBody String description){
       subjectService.changeDescription(id, description);
       return ResponseEntity.ok().body(description);
    }

    @GetMapping("/number-of-sessions/{id}")
    public ResponseEntity<Integer> getNumberOfSessions(@PathVariable(name ="id") Long id) {
        return ResponseEntity.ok().body(subjectService.getNumberOfSessionsForAnSubject(id));
    }

    @GetMapping("/total-time-allotted/{id}")
    public ResponseEntity<Long> getTotalTimeAllotedForAnSubject(@PathVariable(name ="id") Long id){
        return ResponseEntity.ok().body(subjectService.timeAllotedForAnSubject(id));
    }

    @PostMapping
    public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectCreateRequest subjectCreateRequest) {
        return ResponseEntity.ok().body(mapper(subjectService.save(subjectCreateRequest)));
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.deleteById(id);
    }
}
