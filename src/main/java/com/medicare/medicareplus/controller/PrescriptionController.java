package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Prescription;
import com.medicare.medicareplus.repository.PrescriptionRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;


    // GET PRESCRIPTIONS FOR LOGGED-IN PATIENT
    @GetMapping
    public ResponseEntity<?> getPrescriptions(
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


        List<Prescription> prescriptions =
                prescriptionRepository
                        .findByUserId(userId);


        return ResponseEntity.ok(
                prescriptions
        );
    }


    // GET ONE PRESCRIPTION
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long id,
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


        Prescription prescription =
                prescriptionRepository
                        .findById(id)
                        .orElse(null);


        if (prescription == null) {

            return ResponseEntity
                    .notFound()
                    .build();

        }


        // SECURITY CHECK
        if (prescription.getUserId() == null ||
                !prescription.getUserId().equals(userId)) {

            return ResponseEntity
                    .status(403)
                    .body(
                        "You are not allowed to access this prescription."
                    );

        }


        return ResponseEntity.ok(
                prescription
        );
    }
}