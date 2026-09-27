package com.medicare.medicareplus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdminAuthInterceptor adminAuthInterceptor =
            new AdminAuthInterceptor();

    private final DoctorAuthInterceptor doctorAuthInterceptor =
            new DoctorAuthInterceptor();

    private final PatientAuthInterceptor patientAuthInterceptor =
            new PatientAuthInterceptor();


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // =====================================================
        // ADMIN SECURITY
        // =====================================================

        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns(
                        "/admin-dashboard",
                        "/admin-patients",
                        "/admin-doctors",
                        "/admin-appointments",
                        "/api/admin/**"
                )
                .excludePathPatterns(
                        "/admin-login",
                        "/api/admin-auth/login"
                );


        // =====================================================
        // DOCTOR SECURITY
        // =====================================================

        registry.addInterceptor(doctorAuthInterceptor)
                .addPathPatterns(
                        "/doctor-dashboard",
                        "/consultation/**",
                        "/consultation-room/**",
                        "/api/appointments/doctor/**",
                        "/api/consultations/**"
                )
                .excludePathPatterns(
                        "/doctor-login",
                        "/api/doctor-auth/login"
                );


        // =====================================================
        // PATIENT SECURITY
        // =====================================================

        registry.addInterceptor(patientAuthInterceptor)
                .addPathPatterns(
                        "/dashboard",
                        "/profile",
                        "/appointments",
                        "/medical-records",
                        "/prescriptions",
                        "/lab-reports",
                        "/health",
                        "/notifications",
                        "/booking",
                        "/medibot",
                        "/emergency",

                        // Patient APIs
                        "/api/users/me",
                        "/api/users/logout",
                        "/api/appointments/**",
                        "/api/medical-records/**",
                        "/api/prescriptions/**",
                        "/api/lab-reports/**",
                        "/api/health/**",
                        "/api/notifications/**"
                )
                .excludePathPatterns(
                        "/login",
                        "/register",
                        "/api/users/login",
                        "/api/users/register"
                );
    }
}