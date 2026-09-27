package com.medicare.medicareplus.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Doctor;
import com.medicare.medicareplus.repository.DoctorRepository;

@RestController
@RequestMapping("/api/admin/doctors")
public class AdminDoctorAddController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // =========================
    // ADD NEW DOCTOR
    // =========================

    @PostMapping
    public ResponseEntity<?> addDoctor(
            @RequestBody Doctor doctor) {

        if (doctor.getName() == null ||
                doctor.getName().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Doctor name is required.");
        }

        if (doctor.getEmail() == null ||
                doctor.getEmail().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Doctor email is required.");
        }

        if (doctorRepository
                .findByEmail(doctor.getEmail())
                .isPresent()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "A doctor with this email already exists."
                    );
        }

        if (doctor.getPassword() == null ||
                doctor.getPassword().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Doctor password is required.");
        }

        doctor.setPassword(
                passwordEncoder.encode(
                        doctor.getPassword()
                )
        );

        if (doctor.getRating() == 0) {
            doctor.setRating(5.0);
        }

        if (doctor.getAvailability() == null ||
                doctor.getAvailability().trim().isEmpty()) {

            doctor.setAvailability(
                    "Available Today"
            );
        }

        Doctor savedDoctor =
                doctorRepository.save(doctor);

        return ResponseEntity.ok(savedDoctor);
    }


    // =========================
    // UPDATE DOCTOR
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor updatedDoctor) {

        Optional<Doctor> optionalDoctor =
                doctorRepository.findById(id);

        if (optionalDoctor.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        Doctor doctor =
                optionalDoctor.get();


        // =========================
        // UPDATE NAME
        // =========================

        if (updatedDoctor.getName() != null &&
                !updatedDoctor.getName()
                        .trim()
                        .isEmpty()) {

            doctor.setName(
                    updatedDoctor.getName()
            );
        }


        // =========================
        // UPDATE SPECIALIZATION
        // =========================

        if (updatedDoctor.getSpecialization() != null) {

            doctor.setSpecialization(
                    updatedDoctor.getSpecialization()
            );
        }


        // =========================
        // UPDATE QUALIFICATION
        // =========================

        if (updatedDoctor.getQualification() != null) {

            doctor.setQualification(
                    updatedDoctor.getQualification()
            );
        }


        // =========================
        // UPDATE EMAIL
        // =========================

        if (updatedDoctor.getEmail() != null &&
                !updatedDoctor.getEmail()
                        .trim()
                        .isEmpty()) {

            String newEmail =
                    updatedDoctor.getEmail()
                            .trim();

            if (!newEmail.equalsIgnoreCase(
                    doctor.getEmail())) {

                Optional<Doctor> existingDoctor =
                        doctorRepository
                                .findByEmail(newEmail);

                if (existingDoctor.isPresent() &&
                        !existingDoctor.get()
                                .getId()
                                .equals(id)) {

                    return ResponseEntity
                            .badRequest()
                            .body(
                                "Another doctor already uses this email."
                            );
                }

                doctor.setEmail(newEmail);
            }
        }


        // =========================
        // UPDATE PHONE
        // =========================

        if (updatedDoctor.getPhone() != null) {

            doctor.setPhone(
                    updatedDoctor.getPhone()
            );
        }


        // =========================
        // UPDATE EXPERIENCE
        // =========================

        if (updatedDoctor.getExperience() >= 0) {

            doctor.setExperience(
                    updatedDoctor.getExperience()
            );
        }


        // =========================
        // UPDATE RATING
        // =========================

        if (updatedDoctor.getRating() > 0) {

            doctor.setRating(
                    updatedDoctor.getRating()
            );
        }


        // =========================
        // UPDATE PATIENT COUNT
        // =========================

        if (updatedDoctor.getTotalPatients() >= 0) {

            doctor.setTotalPatients(
                    updatedDoctor.getTotalPatients()
            );
        }


        // =========================
        // UPDATE CONSULTATION FEE
        // =========================

        if (updatedDoctor.getConsultationFee() >= 0) {

            doctor.setConsultationFee(
                    updatedDoctor.getConsultationFee()
            );
        }


        // =========================
        // UPDATE AVAILABILITY
        // =========================

        if (updatedDoctor.getAvailability() != null) {

            doctor.setAvailability(
                    updatedDoctor.getAvailability()
            );
        }


        // =========================
        // UPDATE PROFILE IMAGE
        // =========================

        if (updatedDoctor.getProfileImage() != null) {

            doctor.setProfileImage(
                    updatedDoctor.getProfileImage()
            );
        }


        // =========================
        // OPTIONAL PASSWORD CHANGE
        // =========================

        if (updatedDoctor.getPassword() != null &&
                !updatedDoctor.getPassword()
                        .trim()
                        .isEmpty()) {

            doctor.setPassword(
                    passwordEncoder.encode(
                            updatedDoctor.getPassword()
                    )
            );
        }


        // =========================
        // SAVE
        // =========================

        Doctor savedDoctor =
                doctorRepository.save(doctor);

        return ResponseEntity.ok(
                savedDoctor
        );
    }
}