package com.medicare.medicareplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicare.medicareplus.model.HealthData;
import com.medicare.medicareplus.repository.HealthDataRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/health")
public class HealthDataController {

    @Autowired
    private HealthDataRepository healthDataRepository;

    @GetMapping
    public ResponseEntity<?> getHealthData(HttpSession session) {

        Object userIdObject =
                session.getAttribute("userId");

        if (userIdObject == null) {

            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        Long userId =
                Long.valueOf(userIdObject.toString());

        System.out.println("USER ID = " + userId);

        List<HealthData> healthData =
                healthDataRepository.findByUserId(userId);

        System.out.println(
                "HEALTH RECORDS = "
                + healthData.size()
        );

        return ResponseEntity.ok(healthData);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllHealthData() {

        return ResponseEntity.ok(
                healthDataRepository.findAll()
        );
    }
}