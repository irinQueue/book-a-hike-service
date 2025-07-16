package com.project.bookahikeservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized access: You do not have permission to access this resource.\"}");
    }
}
