package com.example.time.box.service;

import com.example.time.box.entity.SubjectEntity;
import com.example.time.box.entity.SubjectSession;
import com.example.time.box.entity.request.SubjectSessionRequest;
import com.example.time.box.exception.EntityNotFoundException;
import com.example.time.box.repository.SubjectRepository;
import com.example.time.box.repository.SubjectSessionRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectSessionServiceTest {

    @Mock
    private SubjectSessionRepository subjectSessionRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectSessionService subjectSessionService;

    private SubjectSession session1;
    private SubjectSession session2;
    private List<SubjectSession> sessions;
    private SubjectEntity subject;
    private SubjectSessionRequest sessionRequest;

    @BeforeEach
    void setUp() {
        session1 = SubjectSession.builder()
                .id(1L)
                .subjectId(1L)
                .startTime(OffsetDateTime.now().minusHours(2))
                .endTime(OffsetDateTime.now().minusHours(1))
                .running(false)
                .timeAllotted(3600L) // 1 hour in seconds
                .build();

        session2 = SubjectSession.builder()
                .id(2L)
                .subjectId(1L)
                .startTime(OffsetDateTime.now().minusMinutes(30))
                .endTime(null)
                .running(true)
                .timeAllotted(0L)
                .build();

        sessions = Arrays.asList(session1, session2);

        subject = SubjectEntity.builder()
                .id(1L)
                .name("Math")
                .description("Mathematics")
                .timeAllotted(120L)
                .numberOfSessions(2)
                .userId(1L)
                .build();

        sessionRequest = SubjectSessionRequest.builder()
                .subjectId(1L)
                .build();
    }

    @Test
    void getAllSubjectSessions() {
        when(subjectSessionRepository.findAll()).thenReturn(sessions);

        List<SubjectSession> result = subjectSessionService.getAllSubjectSessions();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(subjectSessionRepository, times(1)).findAll();
    }

    @Test
    void getSubjectSessionById() {
        when(subjectSessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));

        SubjectSession result = subjectSessionService.getSubjectSessionById(1L);

        assertEquals(1L, result.getId());
        assertEquals(1L, result.getSubjectId());
        verify(subjectSessionRepository, times(1)).findById(1L);
    }

    @Test
    void getSubjectSessionByIdNotFound() {
        when(subjectSessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> subjectSessionService.getSubjectSessionById(999L));
        verify(subjectSessionRepository, times(1)).findById(999L);
    }

    @Test
    void saveSubjectSession() {
        SubjectSession newSession = SubjectSession.builder()
                .id(3L)
                .subjectId(1L)
                .startTime(null)
                .endTime(null)
                .running(false)
                .build();

        when(subjectSessionRepository.save(any(SubjectSession.class))).thenReturn(newSession);

        SubjectSession result = subjectSessionService.saveSubjectSession(sessionRequest);

        assertEquals(3L, result.getId());
        assertEquals(1L, result.getSubjectId());
        assertNull(result.getStartTime());
        assertNull(result.getEndTime());
        assertFalse(result.getRunning());
        verify(subjectSessionRepository, times(1)).save(any(SubjectSession.class));
    }

    @Test
    void setStartTime() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any(SubjectEntity.class))).thenReturn(subject);
        when(subjectSessionRepository.save(any(SubjectSession.class))).thenAnswer(invocation -> {
            SubjectSession session = invocation.getArgument(0);
            session.setId(3L);
            return session;
        });

        subjectSessionService.setStartTime(1L);

        assertEquals(3, subject.getNumberOfSessions());
        verify(subjectRepository, times(1)).findById(1L);
        verify(subjectRepository, times(1)).save(subject);
        verify(subjectSessionRepository, times(1)).save(any(SubjectSession.class));
    }

    @Test
    void setEndTime() {
        when(subjectSessionRepository.findById(anyLong())).thenReturn(Optional.of(session2));
        when(subjectSessionRepository.save(any(SubjectSession.class))).thenReturn(session2);

        subjectSessionService.setEndTime(2L);

        assertNotNull(session2.getEndTime());
        assertFalse(session2.getRunning());
        assertNotNull(session2.getTimeAllotted());
        verify(subjectSessionRepository, times(1)).findById(2L);
        verify(subjectSessionRepository, times(1)).save(session2);
    }

    @Test
    void pauseSession() {
        when(subjectSessionRepository.findById(anyLong())).thenReturn(Optional.of(session2));
        when(subjectSessionRepository.save(any(SubjectSession.class))).thenReturn(session2);

        subjectSessionService.pauseSession(2L);

        assertNotNull(session2.getEndTime());
        assertFalse(session2.getRunning());
        assertTrue(session2.getTimeAllotted() > 0);
        verify(subjectSessionRepository, times(1)).findById(2L);
        verify(subjectSessionRepository, times(1)).save(session2);
    }

    @Test
    void pauseSessionNotRunning() {
        when(subjectSessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        when(subjectSessionRepository.save(any(SubjectSession.class))).thenReturn(session1);

        subjectSessionService.pauseSession(1L);

        assertFalse(session1.getRunning());
        verify(subjectSessionRepository, times(1)).findById(1L);
        verify(subjectSessionRepository, times(1)).save(session1);
    }

    @Test
    void restartSession() {
        when(subjectSessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        when(subjectSessionRepository.save(any(SubjectSession.class))).thenReturn(session1);

        subjectSessionService.restartSession(1L);

        assertNotNull(session1.getStartTime());
        assertNull(session1.getEndTime());
        assertTrue(session1.getRunning());
        verify(subjectSessionRepository, times(1)).findById(1L);
        verify(subjectSessionRepository, times(1)).save(session1);
    }

    @Test
    void durationForAnSessions() {
        when(subjectSessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));

        Long result = subjectSessionService.durationForAnSessions(1L);

        assertEquals(7200L, result);
        verify(subjectSessionRepository, times(1)).findById(1L);
    }

    @Test
    void durationForAnSessionsRunning() {
        // Create a session with start and end time for testing duration calculation
        SubjectSession runningSession = SubjectSession.builder()
                .id(3L)
                .subjectId(1L)
                .startTime(OffsetDateTime.now().minusMinutes(30))
                .endTime(OffsetDateTime.now())
                .running(true)
                .timeAllotted(0L)
                .build();

        when(subjectSessionRepository.findById(anyLong())).thenReturn(Optional.of(runningSession));

        Long result = subjectSessionService.durationForAnSessions(3L);

        assertTrue(result > 0);
        assertNull(runningSession.getStartTime());
        assertNull(runningSession.getEndTime());
        verify(subjectSessionRepository, times(1)).findById(3L);
    }

    @Test
    void deleteSubjectSession() {
        doNothing().when(subjectSessionRepository).deleteById(anyLong());

        subjectSessionService.deleteSubjectSession(1L);

        verify(subjectSessionRepository, times(1)).deleteById(1L);
    }
}
