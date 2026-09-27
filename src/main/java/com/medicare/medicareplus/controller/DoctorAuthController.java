package com.medicare.medicareplus.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Doctor;
import com.medicare.medicareplus.repository.DoctorRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/doctor-auth")
public class DoctorAuthController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpSession session) {

        Optional<Doctor> optionalDoctor =
                doctorRepository.findByEmail(
                        loginRequest.getEmail()
                );

        if (optionalDoctor.isEmpty()) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid doctor email or password.");
        }


        Doctor doctor =
                optionalDoctor.get();

        String enteredPassword =
                loginRequest.getPassword();

        String storedPassword =
                doctor.getPassword();


        // =========================
        // 🔐 BCrypt password check
        // =========================

        boolean passwordMatches =
                storedPassword != null &&
                passwordEncoder.matches(
                        enteredPassword,
                        storedPassword
                );


        // =========================
        // 🔄 BACKWARD COMPATIBILITY
        // =========================
        // Existing doctors whose passwords
        // are still plain text can login once.
        // Their password is then automatically
        // converted to BCrypt.
        // =========================

        if (!passwordMatches &&
                storedPassword != null &&
                enteredPassword.equals(storedPassword)) {

            doctor.setPassword(
                    passwordEncoder.encode(
                            enteredPassword
                    )
            );

            doctorRepository.save(doctor);

            passwordMatches = true;
        }


        // =========================
        // INVALID PASSWORD
        // =========================

        if (!passwordMatches) {

            return ResponseEntity
                    .status(401)
                    .body("Invalid doctor email or password.");
        }


        // =========================
        // CREATE DOCTOR SESSION
        // =========================

        session.setAttribute(
                "doctorId",
                doctor.getId()
        );

        session.setAttribute(
                "doctorName",
                doctor.getName()
        );

        session.setAttribute(
                "doctorEmail",
                doctor.getEmail()
        );


        // =========================
        // SAFE RESPONSE
        // =========================

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "id",
                doctor.getId()
        );

        response.put(
                "name",
                doctor.getName()
        );

        response.put(
                "email",
                doctor.getEmail()
        );

        response.put(
                "specialization",
                doctor.getSpecialization()
        );


        return ResponseEntity.ok(response);
    }


    // =========================
    // CURRENT DOCTOR
    // =========================

    @GetMapping("/me")
    public ResponseEntity<?> currentDoctor(
            HttpSession session) {

        Object doctorId =
                session.getAttribute("doctorId");


        if (doctorId == null) {

            return ResponseEntity
                    .status(401)
                    .body("Doctor not logged in.");
        }


        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "id",
                doctorId
        );

        response.put(
                "name",
                session.getAttribute("doctorName")
        );

        response.put(
                "email",
                session.getAttribute("doctorEmail")
        );


        return ResponseEntity.ok(response);
    }


    // =========================
    // LOGOUT
    // =========================

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpSession session) {

        session.removeAttribute("doctorId");
        session.removeAttribute("doctorName");
        session.removeAttribute("doctorEmail");


        return ResponseEntity.ok(
                "Doctor logged out successfully."
        );
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