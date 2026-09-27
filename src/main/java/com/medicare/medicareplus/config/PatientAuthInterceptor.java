package com.medicare.medicareplus.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class PatientAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String path = request.getRequestURI();

        // Public pages
        if (path.equals("/")
                || path.equals("/login")
                || path.equals("/register")
                || path.equals("/doctor-login")
                || path.equals("/admin-login")
                || path.startsWith("/doctor/")) {

            return true;
        }

        // Public authentication endpoints
        if (path.equals("/api/users/login")
                || path.equals("/api/users/register")) {

            return true;
        }

        // Check patient session
        Object userId =
                request.getSession()
                        .getAttribute("userId");

        if (userId != null) {
            return true;
        }

        // Protected API requests
        if (path.startsWith("/api/")) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Patient authentication required.\"}"
            );

            return false;
        }

        // Protected pages
        response.sendRedirect("/login");

        return false;
    }
}