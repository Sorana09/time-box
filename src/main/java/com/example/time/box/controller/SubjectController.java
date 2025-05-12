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

    @GetMapping("/number-of-sessions/{id}")
    public ResponseEntity<Integer> getNumberOfSessions(@PathVariable Long id) {
        return ResponseEntity.ok().body(subjectService.getNumberOfSessionsForAnSubject(id));
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
