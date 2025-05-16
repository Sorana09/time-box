package com.example.time.box.aspect;

import com.example.time.box.domain.UserDto;
import com.example.time.box.metrics.ViewUserMetric;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@AllArgsConstructor
@Component
public class UserAspect {
    private final ViewUserMetric viewUserMetric;

    @AfterReturning(
            value="execution(* com.example.time.box.controller.UserController.signUp(..))",
            returning = "responseEntity"
    )
    public void afterReturningSignup(ResponseEntity<UserDto> responseEntity){
        if(responseEntity.getStatusCode().is2xxSuccessful()){

            UserDto userDto = responseEntity.getBody();

            if (userDto != null){
                viewUserMetric.registerViewForUser(userDto.getId());
            }

        }
    }

}
