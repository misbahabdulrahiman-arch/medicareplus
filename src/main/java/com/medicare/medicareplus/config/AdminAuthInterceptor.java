package com.medicare.medicareplus.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String path = request.getRequestURI();

        // Allow admin login page
        if (path.equals("/admin-login")) {
            return true;
        }

        // Allow login API
        if (path.equals("/api/admin-auth/login")) {
            return true;
        }

        // Check admin session
        Object adminId =
                request.getSession().getAttribute("adminId");

        if (adminId != null) {
            return true;
        }

        // Protect admin APIs
        if (path.startsWith("/api/admin/")) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Admin authentication required.\"}"
            );

            return false;
        }

        // Protect admin pages
        response.sendRedirect("/admin-login");

        return false;
    }
}