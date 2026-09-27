package com.medicare.medicareplus.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.User;
import com.medicare.medicareplus.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody User user) {

        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {

            return ResponseEntity.badRequest()
                    .body("An account with this email already exists.");
        }

        user.setRole("PATIENT");

        // 🔐 Store password as BCrypt hash
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        User savedUser =
                userRepository.save(user);

        return ResponseEntity.ok(
                createSafeUserResponse(savedUser)
        );
    }


    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequest loginRequest,
            HttpSession session) {

        Optional<User> optionalUser =
                userRepository.findByEmail(
                        loginRequest.getEmail()
                );

        if (optionalUser.isEmpty()) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid email or password.");
        }

        User user = optionalUser.get();

        String enteredPassword =
                loginRequest.getPassword();

        String storedPassword =
                user.getPassword();


        // =========================
        // 🔐 BCrypt password check
        // =========================

        boolean passwordMatches =
                passwordEncoder.matches(
                        enteredPassword,
                        storedPassword
                );


        // =========================
        // 🔄 BACKWARD COMPATIBILITY
        // =========================
        // This allows users created before
        // BCrypt was added to still login.
        //
        // Once they successfully login,
        // their old plain-text password is
        // immediately converted to BCrypt.
        // =========================

        if (!passwordMatches &&
                enteredPassword.equals(storedPassword)) {

            user.setPassword(
                    passwordEncoder.encode(
                            enteredPassword
                    )
            );

            userRepository.save(user);

            passwordMatches = true;
        }


        // =========================
        // INVALID PASSWORD
        // =========================

        if (!passwordMatches) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid email or password.");
        }


        // =========================
        // CREATE SESSION
        // =========================

        session.setAttribute(
                "userId",
                user.getId()
        );

        session.setAttribute(
                "userName",
                user.getName()
        );

        session.setAttribute(
                "userEmail",
                user.getEmail()
        );

        session.setAttribute(
                "userRole",
                user.getRole()
        );


        return ResponseEntity.ok(
                createSafeUserResponse(user)
        );
    }


    // =========================
    // GET CURRENT USER
    // =========================

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            HttpSession session) {

        Object userId =
                session.getAttribute("userId");

        if (userId == null) {

            return ResponseEntity
                    .status(401)
                    .body("Not logged in.");
        }


        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "id",
                session.getAttribute("userId")
        );

        response.put(
                "name",
                session.getAttribute("userName")
        );

        response.put(
                "email",
                session.getAttribute("userEmail")
        );

        response.put(
                "role",
                session.getAttribute("userRole")
        );


        // =========================
        // GET PHONE FROM DATABASE
        // =========================

        Optional<User> optionalUser =
                userRepository.findById(
                        Long.valueOf(
                                userId.toString()
                        )
                );

        if (optionalUser.isPresent()) {

            response.put(
                    "phone",
                    optionalUser.get().getPhone()
            );
        }


        return ResponseEntity.ok(response);
    }


    // =========================
    // UPDATE PROFILE
    // =========================

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(
            @RequestBody User updatedUser,
            HttpSession session) {

        Object userIdObject =
                session.getAttribute("userId");

        if (userIdObject == null) {

            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }


        Long userId =
                Long.valueOf(
                        userIdObject.toString()
                );


        Optional<User> optionalUser =
                userRepository.findById(userId);

        if (optionalUser.isEmpty()) {

            return ResponseEntity
                    .status(404)
                    .body("User not found.");
        }


        User user =
                optionalUser.get();


        // =========================
        // CHECK EMAIL CHANGE
        // =========================

        if (updatedUser.getEmail() != null &&
                !updatedUser.getEmail()
                        .equalsIgnoreCase(
                                user.getEmail()
                        )) {

            Optional<User> emailUser =
                    userRepository.findByEmail(
                            updatedUser.getEmail()
                    );

            if (emailUser.isPresent() &&
                    !emailUser.get()
                            .getId()
                            .equals(userId)) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "That email is already being used."
                        );
            }

            user.setEmail(
                    updatedUser.getEmail()
            );
        }


        // =========================
        // UPDATE NAME
        // =========================

        if (updatedUser.getName() != null) {

            user.setName(
                    updatedUser.getName()
            );
        }


        // =========================
        // UPDATE PHONE
        // =========================

        if (updatedUser.getPhone() != null) {

            user.setPhone(
                    updatedUser.getPhone()
            );
        }


        // =========================
        // SAVE USER
        // =========================

        User savedUser =
                userRepository.save(user);


        // =========================
        // UPDATE SESSION
        // =========================

        session.setAttribute(
                "userName",
                savedUser.getName()
        );

        session.setAttribute(
                "userEmail",
                savedUser.getEmail()
        );


        return ResponseEntity.ok(
                createSafeUserResponse(savedUser)
        );
    }


    // =========================
    // LOGOUT
    // =========================

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok(
                "Logged out successfully."
        );
    }


    // =========================
    // SAFE USER RESPONSE
    // =========================

    private Map<String, Object>
    createSafeUserResponse(User user) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "id",
                user.getId()
        );

        response.put(
                "name",
                user.getName()
        );

        response.put(
                "email",
                user.getEmail()
        );

        response.put(
                "phone",
                user.getPhone()
        );

        response.put(
                "role",
                user.getRole()
        );

        return response;
    }


    // =========================
    // LOGIN REQUEST
    // =========================

    public static class LoginRequest {

        private String email;

        private String password;


        public String getEmail() {
            return email;
        }


        public void setEmail(String email) {
            this.email = email;
        }


        public String getPassword() {
            return password;
        }


        public void setPassword(String password) {
            this.password = password;
        }
    }
}