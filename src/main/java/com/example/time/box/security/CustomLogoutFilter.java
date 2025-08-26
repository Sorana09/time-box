package com.example.time.box.security;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("/sessions".equals(httpRequest.getRequestURI()) &&
                "DELETE".equals(httpRequest.getMethod())) {

            Cookie cookie = new Cookie("JSESSIONID", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            httpResponse.addCookie(cookie);
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}