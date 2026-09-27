package com.medicare.medicareplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/booking",
                    "/appointments",
                    "/doctor/**",
                    "/profile",
                    "/dashboard",
                    "/medibot",
                    "/medical-records",
                    "/prescriptions",
                    "/lab-reports",
                    "/health",
                    "/notifications",
                    "/doctor-login",
                    "/admin-login"
                ).permitAll()

                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico"
                ).permitAll()

                .requestMatchers(
                    "/api/admin-auth/login",
                    "/api/doctor-auth/login",
                    "/api/auth/**"
                ).permitAll()

                .anyRequest().permitAll()
            );

        return http.build();
    }
}