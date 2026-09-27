package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.LabReport;
import com.medicare.medicareplus.repository.LabReportRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/lab-reports")
public class LabReportController {

    @Autowired
    private LabReportRepository labReportRepository;

    @GetMapping
    public ResponseEntity<?> getLabReports(HttpSession session) {

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

        List<LabReport> reports =
                labReportRepository.findByUserId(userId);

        return ResponseEntity.ok(reports);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getLabReport(
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

        LabReport report =
                labReportRepository
                        .findById(id)
                        .orElse(null);

        if (report == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (report.getUserId() == null ||
                !report.getUserId().equals(userId)) {

            return ResponseEntity
                    .status(403)
                    .body(
                        "You are not allowed to access this lab report."
                    );
        }

        return ResponseEntity.ok(report);
    }
}