package com.example.time.box.aspect;

import com.example.time.box.entity.RoomEntity;
import com.example.time.box.metrics.RoomMetric;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RoomAspect {
    private final RoomMetric roomMetric;

    @AfterReturning("execution(* com.example.time.box.service.RoomService.createRoom(..))")
    public void afterRoomCreated() {
        roomMetric.countRoomCreation();
    }

    @AfterReturning("execution(* com.example.time.box.service.RoomService.addParticipant(..))")
    public void afterParticipantAdded() {
        roomMetric.countParticipantAdded();
    }

    @AfterReturning(
        pointcut = "execution(* com.example.time.box.service.RoomService.findByInvitationToken(String))",
        returning = "result")
    public void afterRoomAccessed(JoinPoint joinPoint, Object result) {
        if (result instanceof java.util.Optional && ((java.util.Optional<?>) result).isPresent()) {
            String token = (String) joinPoint.getArgs()[0];
            roomMetric.registerRoomAccess(token);
        }
    }

    @AfterReturning(
        pointcut = "execution(* com.example.time.box.service.RoomService.saveRoom(..)) && args(room)",
        argNames = "room")
    public void afterRoomSaved(RoomEntity room) {
        if (room.getIsActive() != null) {
            roomMetric.countRoomStatusChange(room.getIsActive());
        }
        
        if (room.getChatEnabled() != null) {
            roomMetric.countChatStatusChange(room.getChatEnabled());
        }
    }
}