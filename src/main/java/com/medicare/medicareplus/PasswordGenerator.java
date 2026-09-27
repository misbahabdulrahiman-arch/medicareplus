package com.medicare.medicareplus;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String hash = encoder.encode("admin123");

        System.out.println("=================================");
        System.out.println("BCrypt Password Hash:");
        System.out.println(hash);
        System.out.println("=================================");
    }
}