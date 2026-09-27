package com.medicare.medicareplus.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class DoctorAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String path = request.getRequestURI();

        // Doctor login must remain accessible
        if (path.equals("/doctor-login")) {
            return true;
        }

        // Doctor login API must remain accessible
        if (path.equals("/api/doctor-auth/login")) {
            return true;
        }

        // Check doctor session
        Object doctorId =
                request.getSession()
                        .getAttribute("doctorId");

        // Doctor is logged in
        if (doctorId != null) {
            return true;
        }

        // Protect doctor APIs
        if (path.startsWith("/api/appointments/doctor")) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Doctor authentication required.\"}"
            );

            return false;
        }

        // Protect doctor dashboard
        response.sendRedirect("/doctor-login");

        return false;
    }
}