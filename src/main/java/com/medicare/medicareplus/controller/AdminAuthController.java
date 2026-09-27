package com.medicare.medicareplus.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.medicare.medicareplus.model.User;
import com.medicare.medicareplus.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class AdminAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // ===============================
    // ADMIN LOGIN PAGE
    // ===============================

    @GetMapping("/admin-login")
    public String adminLogin() {
        return "admin-login";
    }


    // ===============================
    // ADMIN LOGIN API
    // ===============================

    @PostMapping("/api/admin-auth/login")
    @ResponseBody
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request,
            HttpSession session) {

        String email = request.get("email");
        String password = request.get("password");

        if (email == null || password == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Email and password are required.");
        }


        Optional<User> userOptional =
                userRepository.findTopByEmailAndRoleIgnoreCase(
                        email.trim(),
                        "ADMIN"
                );


        if (userOptional.isEmpty()) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid admin credentials.");
        }


        User user = userOptional.get();


        // Make sure this account is actually an admin

        if (user.getRole() == null ||
            !"ADMIN".equalsIgnoreCase(user.getRole())) {

            return ResponseEntity
                    .status(403)
                    .body("This account does not have administrator access.");
        }


        // Temporary plaintext password check.
        // We will replace this with password hashing later.
        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid admin credentials.");
        }


        // Store admin information in session

        session.setAttribute(
                "adminId",
                user.getId()
        );

        session.setAttribute(
                "adminName",
                user.getName()
        );

        session.setAttribute(
                "adminEmail",
                user.getEmail()
        );


        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "message",
                "Admin login successful."
        );

        response.put(
                "name",
                user.getName()
        );

        response.put(
                "email",
                user.getEmail()
        );


        return ResponseEntity.ok(response);
    }


    // ===============================
    // CHECK ADMIN SESSION
    // ===============================

    @GetMapping("/api/admin-auth/me")
    @ResponseBody
    public ResponseEntity<?> currentAdmin(
            HttpSession session) {

        Object adminId =
                session.getAttribute("adminId");


        if (adminId == null) {

            return ResponseEntity
                    .status(401)
                    .body("Not logged in.");
        }


        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "id",
                adminId
        );

        response.put(
                "name",
                session.getAttribute("adminName")
        );

        response.put(
                "email",
                session.getAttribute("adminEmail")
        );


        return ResponseEntity.ok(response);
    }


    // ===============================
    // ADMIN LOGOUT
    // ===============================

    @PostMapping("/api/admin-auth/logout")
    @ResponseBody
    public ResponseEntity<?> logout(
            HttpSession session) {

        session.removeAttribute("adminId");
        session.removeAttribute("adminName");
        session.removeAttribute("adminEmail");

        return ResponseEntity.ok(
                "Admin logged out successfully."
        );
    }
}