package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.User;
import com.medicare.medicareplus.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/patients")
public class AdminPatientController {

    @Autowired
    private UserRepository userRepository;


    // =========================
    // GET ALL PATIENTS
    // =========================

    @GetMapping
    public ResponseEntity<?> getAllPatients() {

        List<User> patients =
                userRepository.findByRoleIgnoreCase(
                        "PATIENT"
                );

        return ResponseEntity.ok(patients);
    }


    // =========================
    // GET ONE PATIENT
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatient(
            @PathVariable Long id) {

        User patient =
                userRepository
                        .findById(id)
                        .orElse(null);

        if (patient == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        // Make sure this user is actually a patient
        if (patient.getRole() == null ||
                !patient.getRole()
                        .equalsIgnoreCase("PATIENT")) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(patient);
    }


    // =========================
    // DELETE PATIENT
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(
            @PathVariable Long id) {

        User patient =
                userRepository
                        .findById(id)
                        .orElse(null);

        if (patient == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        // Never allow this endpoint
        // to delete an admin or another role
        if (patient.getRole() == null ||
                !patient.getRole()
                        .equalsIgnoreCase("PATIENT")) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Only patient accounts can be deleted."
                    );
        }

        userRepository.delete(patient);

        return ResponseEntity.ok(
                "Patient deleted successfully."
        );
    }
}