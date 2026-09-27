package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.MedicalRecord;
import com.medicare.medicareplus.repository.MedicalRecordRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;


    // GET ALL MEDICAL RECORDS FOR LOGGED-IN PATIENT
    @GetMapping
    public ResponseEntity<?> getMedicalRecords(
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

        List<MedicalRecord> records =
                medicalRecordRepository
                        .findByUserId(userId);

        return ResponseEntity.ok(records);
    }


    // GET ONE MEDICAL RECORD
    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalRecord(
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

        MedicalRecord record =
                medicalRecordRepository
                        .findById(id)
                        .orElse(null);

        if (record == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        // SECURITY CHECK
        if (record.getUserId() == null ||
                !record.getUserId().equals(userId)) {

            return ResponseEntity
                    .status(403)
                    .body("You are not allowed to access this medical record.");
        }

        return ResponseEntity.ok(record);
    }
}