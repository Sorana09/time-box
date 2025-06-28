package com.example.time.box.service;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.request.SubjectCreateRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectSessionService subjectSessionService;

    @InjectMocks
    private SubjectService subjectService;

    private SubjectEntity subject1;
    private SubjectEntity subject2;
    private List<SubjectEntity> subjects;
    private SubjectSession session1;
    private SubjectSession session2;
    private List<SubjectSession> sessions;
    private SubjectCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        subject1 = SubjectEntity.builder()
            .id(1L)
            .name("Math")
            .description("Mathematics")
            .timeAllotted(120L)
            .numberOfSessions(2)
            .userId(1L)
            .build();

        subject2 = SubjectEntity.builder()
            .id(2L)
            .name("Science")
            .description("Science studies")
            .timeAllotted(180L)
            .numberOfSessions(3)
            .userId(1L)
            .build();

        subjects = Arrays.asList(subject1, subject2);

        session1 = SubjectSession.builder()
            .id(1L)
            .subjectId(1L)
            .startTime(OffsetDateTime.now())
            .endTime(OffsetDateTime.now().plusHours(1))
            .running(false)
            .timeAllotted(60L)
            .build();

        session2 = SubjectSession.builder()
            .id(2L)
            .subjectId(1L)
            .startTime(OffsetDateTime.now())
            .endTime(OffsetDateTime.now().plusHours(2))
            .running(false)
            .timeAllotted(120L)
            .build();

        sessions = Arrays.asList(session1, session2);

        createRequest = SubjectCreateRequest.builder()
            .name("New Subject")
            .description("New Description")
            .userId(1L)
            .build();
    }

    @Test
    void getNumberOfSessionsForAnSubject() {
        when(subjectSessionService.getAllSubjectSessions()).thenReturn(sessions);

        Integer result = subjectService.getNumberOfSessionsForAnSubject(1L);

        assertEquals(2, result);
        verify(subjectSessionService, times(1)).getAllSubjectSessions();
    }

    @Test
    void timeAllotedForAnSubject() {
        when(subjectSessionService.getAllSubjectSessions()).thenReturn(sessions);
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(subject1));

        Long result = subjectService.timeAllotedForAnSubject(1L);

        // 120 (subject) + 60 (session1) + 120 (session2) = 300
        assertEquals(300L, result);
        verify(subjectSessionService, times(1)).getAllSubjectSessions();
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    void findAll() {
        when(subjectRepository.findAll()).thenReturn(subjects);

        List<SubjectEntity> result = subjectService.findAll();

        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).getName());
        assertEquals("Science", result.get(1).getName());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void findAllByUserId() {
        when(subjectRepository.findByUserId(anyLong())).thenReturn(subjects);

        List<SubjectEntity> result = subjectService.findAllByUserId(1L);

        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).getName());
        assertEquals("Science", result.get(1).getName());
        verify(subjectRepository, times(1)).findByUserId(1L);
    }

    @Test
    void findById() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(subject1));

        SubjectEntity result = subjectService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Math", result.getName());
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdNotFound() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> subjectService.findById(999L));
        verify(subjectRepository, times(1)).findById(999L);
    }

    @Test
    void changeDescription() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(subject1));
        when(subjectRepository.save(any(SubjectEntity.class))).thenReturn(subject1);

        String result = subjectService.changeDescription(1L, "Updated Description");

        assertEquals("Updated Description", result);
        assertEquals("Updated Description", subject1.getDescription());
        verify(subjectRepository, times(1)).findById(1L);
        verify(subjectRepository, times(1)).save(subject1);
    }

    @Test
    void save() {
        SubjectEntity newSubject = SubjectEntity.builder()
            .id(3L)
            .name("New Subject")
            .description("New Description")
            .userId(1L)
            .build();

        when(subjectRepository.save(any(SubjectEntity.class))).thenReturn(newSubject);

        SubjectEntity result = subjectService.save(createRequest);

        assertEquals(3L, result.getId());
        assertEquals("New Subject", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(1L, result.getUserId());
        verify(subjectRepository, times(1)).save(any(SubjectEntity.class));
    }

    @Test
    void deleteById() {
        doNothing().when(subjectRepository).deleteById(anyLong());

        subjectService.deleteById(1L);

        verify(subjectRepository, times(1)).deleteById(1L);
    }
}