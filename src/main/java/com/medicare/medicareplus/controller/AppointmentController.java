package com.medicare.medicareplus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.medicareplus.model.Appointment;
import com.medicare.medicareplus.model.Notification;
import com.medicare.medicareplus.model.User;
import com.medicare.medicareplus.repository.AppointmentRepository;
import com.medicare.medicareplus.repository.NotificationRepository;
import com.medicare.medicareplus.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;


    // =========================================================
    // CREATE APPOINTMENT
    // =========================================================

    @PostMapping
    public ResponseEntity<?> createAppointment(
            @RequestBody Appointment appointment,
            HttpSession session) {

        Object userIdObject = session.getAttribute("userId");
        Object userNameObject = session.getAttribute("userName");

        if (userIdObject == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login before booking an appointment.");
        }

        Long userId = Long.valueOf(userIdObject.toString());

        String userName = userNameObject != null
                ? userNameObject.toString()
                : "Patient";


        // =====================================================
        // VALIDATE DOCTOR
        // =====================================================

        if (appointment.getDoctorId() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Doctor information is missing.");
        }


        // =====================================================
        // VALIDATE DATE/TIME
        // =====================================================

        if (appointment.getAppointmentDate() == null
                || appointment.getAppointmentDate().isBlank()
                || appointment.getAppointmentTime() == null
                || appointment.getAppointmentTime().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Appointment date and time are required.");
        }


        // =====================================================
        // PREVENT DOUBLE BOOKING
        // =====================================================

        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                                appointment.getDoctorId(),
                                appointment.getAppointmentDate(),
                                appointment.getAppointmentTime(),
                                "CANCELLED"
                        );

        if (alreadyBooked) {
            return ResponseEntity
                    .status(409)
                    .body(
                        "This doctor already has an appointment at this date and time."
                    );
        }


        // =====================================================
        // CONNECT APPOINTMENT TO LOGGED-IN PATIENT
        // =====================================================

        appointment.setPatientId(userId);
        appointment.setPatientName(userName);

        appointment.setStatus("CONFIRMED");


        // =====================================================
        // SAVE
        // =====================================================

        Appointment savedAppointment =
                appointmentRepository.save(appointment);


        // =====================================================
        // PATIENT NOTIFICATION
        // =====================================================

        Notification notification = new Notification();

        notification.setUserId(userId);

        notification.setTitle(
                "Appointment Confirmed"
        );

        notification.setMessage(
                "Your appointment with "
                + appointment.getDoctorName()
                + " has been confirmed for "
                + appointment.getAppointmentDate()
                + " at "
                + appointment.getAppointmentTime()
                + "."
        );

        notification.setType("APPOINTMENT");

        notification.setNotificationDate(
                appointment.getAppointmentDate()
        );

        notification.setReadStatus(false);

        notificationRepository.save(notification);


        return ResponseEntity.ok(savedAppointment);
    }


    // =========================================================
    // GET PATIENT APPOINTMENTS
    // =========================================================

    @GetMapping
    public ResponseEntity<?> getAppointments(
            HttpSession session) {

        Object userIdObject = session.getAttribute("userId");

        if (userIdObject == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        Long userId =
                Long.valueOf(userIdObject.toString());


        // First use the REAL patient ID
        List<Appointment> appointments =
                appointmentRepository.findByPatientId(userId);


        // =====================================================
        // BACKWARD COMPATIBILITY
        // =====================================================
        // Existing appointments created before this upgrade
        // may not have patientId yet.

        if (appointments.isEmpty()) {

            Object userNameObject =
                    session.getAttribute("userName");

            if (userNameObject != null) {

                String userName =
                        userNameObject.toString();

                appointments =
                        appointmentRepository
                                .findByPatientName(userName);
            }
        }


        return ResponseEntity.ok(appointments);
    }


    // =========================================================
    // GET DOCTOR APPOINTMENTS
    // =========================================================

    @GetMapping("/doctor")
    public ResponseEntity<?> getDoctorAppointments(
            HttpSession session) {

        Object doctorIdObject =
                session.getAttribute("doctorId");

        if (doctorIdObject == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login as a doctor first.");
        }

        Long doctorId =
                Long.valueOf(
                        doctorIdObject.toString()
                );

        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorId(doctorId);

        return ResponseEntity.ok(appointments);
    }


    // =========================================================
    // CANCEL APPOINTMENT
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelAppointment(
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


        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElse(null);

        if (appointment == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }


        // =====================================================
        // REAL OWNERSHIP CHECK
        // =====================================================

        if (appointment.getPatientId() != null) {

            if (!appointment.getPatientId().equals(userId)) {

                return ResponseEntity
                        .status(403)
                        .body(
                            "You cannot cancel this appointment."
                        );
            }

        } else {

            // Backward compatibility for old appointments

            String loggedInUser =
                    String.valueOf(
                            session.getAttribute("userName")
                    );

            if (!loggedInUser.equals(
                    appointment.getPatientName()
            )) {

                return ResponseEntity
                        .status(403)
                        .body(
                            "You cannot cancel this appointment."
                        );
            }

            // Upgrade old appointment automatically
            appointment.setPatientId(userId);
        }


        // =====================================================
        // CANCEL
        // =====================================================

        appointment.setStatus("CANCELLED");

        appointmentRepository.save(appointment);


        // =====================================================
        // NOTIFICATION
        // =====================================================

        Notification notification =
                new Notification();

        notification.setUserId(userId);

        notification.setTitle(
                "Appointment Cancelled"
        );

        notification.setMessage(
                "Your appointment with "
                + appointment.getDoctorName()
                + " on "
                + appointment.getAppointmentDate()
                + " has been cancelled."
        );

        notification.setType("APPOINTMENT");

        notification.setNotificationDate(
                appointment.getAppointmentDate()
        );

        notification.setReadStatus(false);

        notificationRepository.save(notification);


        return ResponseEntity.ok(
                "Appointment cancelled successfully."
        );
    }


    // =========================================================
    // RESCHEDULE APPOINTMENT
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody Appointment updatedAppointment,
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


        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElse(null);

        if (appointment == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }


        // =====================================================
        // OWNERSHIP CHECK
        // =====================================================

        if (appointment.getPatientId() != null) {

            if (!appointment.getPatientId().equals(userId)) {

                return ResponseEntity
                        .status(403)
                        .body(
                            "You cannot reschedule this appointment."
                        );
            }

        } else {

            String loggedInUser =
                    String.valueOf(
                            session.getAttribute("userName")
                    );

            if (!loggedInUser.equals(
                    appointment.getPatientName()
            )) {

                return ResponseEntity
                        .status(403)
                        .body(
                            "You cannot reschedule this appointment."
                        );
            }

            appointment.setPatientId(userId);
        }


        // =====================================================
        // CHECK NEW SLOT
        // =====================================================

        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                                appointment.getDoctorId(),
                                updatedAppointment.getAppointmentDate(),
                                updatedAppointment.getAppointmentTime(),
                                "CANCELLED"
                        );

        if (alreadyBooked) {
            return ResponseEntity
                    .status(409)
                    .body(
                        "This doctor already has an appointment at the selected time."
                    );
        }


        // =====================================================
        // UPDATE
        // =====================================================

        appointment.setAppointmentDate(
                updatedAppointment.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                updatedAppointment.getAppointmentTime()
        );

        appointment.setStatus("CONFIRMED");

        appointmentRepository.save(appointment);


        // =====================================================
        // NOTIFICATION
        // =====================================================

        Notification notification =
                new Notification();

        notification.setUserId(userId);

        notification.setTitle(
                "Appointment Rescheduled"
        );

        notification.setMessage(
                "Your appointment with "
                + appointment.getDoctorName()
                + " has been rescheduled to "
                + appointment.getAppointmentDate()
                + " at "
                + appointment.getAppointmentTime()
                + "."
        );

        notification.setType("APPOINTMENT");

        notification.setNotificationDate(
                appointment.getAppointmentDate()
        );

        notification.setReadStatus(false);

        notificationRepository.save(notification);


        return ResponseEntity.ok(appointment);
    }


    // =========================================================
    // DOCTOR UPDATES APPOINTMENT STATUS
    // =========================================================

    @PutMapping("/doctor/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            HttpSession session) {

        Object doctorIdObject =
                session.getAttribute("doctorId");

        if (doctorIdObject == null) {
            return ResponseEntity
                    .status(401)
                    .body(
                        "Please login as a doctor first."
                    );
        }

        Long doctorId =
                Long.valueOf(
                        doctorIdObject.toString()
                );


        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElse(null);

        if (appointment == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }


        // =====================================================
        // DOCTOR OWNERSHIP
        // =====================================================

        if (appointment.getDoctorId() == null
                || !appointment.getDoctorId().equals(doctorId)) {

            return ResponseEntity
                    .status(403)
                    .body(
                        "You cannot update this appointment."
                    );
        }


        // =====================================================
        // VALIDATE STATUS
        // =====================================================

        String newStatus =
                status.toUpperCase();


        if (!newStatus.equals("CONFIRMED")
                && !newStatus.equals("COMPLETED")
                && !newStatus.equals("CANCELLED")) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Invalid appointment status."
                    );
        }


        appointment.setStatus(newStatus);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);


        // =====================================================
        // FIND PATIENT
        // =====================================================

        User patient = null;

        if (appointment.getPatientId() != null) {

            patient =
                    userRepository
                            .findById(
                                    appointment.getPatientId()
                            )
                            .orElse(null);

        } else {

            patient =
                    userRepository
                            .findByName(
                                    appointment.getPatientName()
                            )
                            .orElse(null);
        }


        // =====================================================
        // PATIENT NOTIFICATION
        // =====================================================

        if (patient != null) {

            // Upgrade old appointment
            if (appointment.getPatientId() == null) {

                appointment.setPatientId(
                        patient.getId()
                );

                appointmentRepository.save(appointment);
            }


            Notification notification =
                    new Notification();

            notification.setUserId(
                    patient.getId()
            );


            String formattedStatus =
                    newStatus.substring(0, 1)
                            .toUpperCase()
                    +
                    newStatus.substring(1)
                            .toLowerCase();


            notification.setTitle(
                    "Appointment "
                    + formattedStatus
            );


            notification.setMessage(
                    "Your appointment with "
                    + appointment.getDoctorName()
                    + " on "
                    + appointment.getAppointmentDate()
                    + " at "
                    + appointment.getAppointmentTime()
                    + " is now "
                    + formattedStatus
                    + "."
            );


            notification.setType(
                    "APPOINTMENT"
            );


            notification.setNotificationDate(
                    appointment.getAppointmentDate()
            );


            notification.setReadStatus(
                    false
            );


            notificationRepository.save(
                    notification
            );
        }


        return ResponseEntity.ok(
                savedAppointment
        );
    }
}