package com.example.time.box.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userIdProtectionInterceptor())
                .addPathPatterns("/users/**", "/subjects/**", "/sessions/**", "/achievements/**")
                .excludePathPatterns("/auth/**", "/actuator/**", "/ws-chat/**", "/topic/**", "/app/**");
    }

    @Bean
    public HandlerInterceptor userIdProtectionInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String path = request.getRequestURI();

                // Only enforce when a numeric userId appears in the path
                Long pathUserId = extractUserIdFromPath(path);
                if (pathUserId == null) {
                    return true;
                }

                // Read authenticated principal from Spring Security context
                org.springframework.security.core.Authentication auth =
                        org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || auth.getPrincipal() == null) {
                    response.setStatus(401);
                    return false;
                }

                Long principalUserId;
                try {
                    principalUserId = (auth.getPrincipal() instanceof Long)
                            ? (Long) auth.getPrincipal()
                            : Long.valueOf(String.valueOf(auth.getPrincipal()));
                } catch (Exception ignored) {
                    response.setStatus(401);
                    return false;
                }

                if (!principalUserId.equals(pathUserId)) {
                    response.setStatus(403);
                    return false;
                }
                return true;
            }
        };
    }

    private Long extractUserIdFromPath(String path) {
        int idx = path.indexOf("/users/");
        if (idx == -1) return null;
        String rest = path.substring(idx + 7);
        String[] parts = rest.split("/", 2);
        try {
            return Long.parseLong(parts[0]);
        } catch (Exception ignored) {
            return null;
        }
    }
}
