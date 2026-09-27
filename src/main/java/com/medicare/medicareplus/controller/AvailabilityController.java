package com.medicare.medicareplus.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicare.medicareplus.model.Doctor;
import com.medicare.medicareplus.repository.AppointmentRepository;
import com.medicare.medicareplus.repository.DoctorRepository;

@RestController
@RequestMapping("/api/doctors")
public class AvailabilityController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;


    // =========================================================
    // GET AVAILABLE TIME SLOTS
    // =========================================================

    @GetMapping("/available-slots")
    public ResponseEntity<?> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam String date) {

        // =====================================================
        // FIND DOCTOR
        // =====================================================

        Doctor doctor =
                doctorRepository
                        .findById(doctorId)
                        .orElse(null);

        if (doctor == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }


        // =====================================================
        // VALIDATE DATE
        // =====================================================

        LocalDate selectedDate;

        try {

            selectedDate =
                    LocalDate.parse(date);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Invalid date. Use YYYY-MM-DD."
                    );
        }


        // =====================================================
        // CHECK IF DATE IS IN THE PAST
        // =====================================================

        if (selectedDate.isBefore(LocalDate.now())) {

            return ResponseEntity.ok(
                    new ArrayList<>()
            );
        }


        // =====================================================
        // CHECK WORKING DAY
        // =====================================================

        DayOfWeek selectedDay =
                selectedDate.getDayOfWeek();

        String dayName =
                selectedDay
                        .toString()
                        .toUpperCase();


        String workingDays =
                doctor.getWorkingDays();


        if (workingDays == null
                || workingDays.isBlank()) {

            return ResponseEntity.ok(
                    new ArrayList<>()
            );
        }


        boolean doctorWorksToday =
                workingDays
                        .toUpperCase()
                        .contains(dayName);


        if (!doctorWorksToday) {

            return ResponseEntity.ok(
                    new ArrayList<>()
            );
        }


        // =====================================================
        // CHECK DOCTOR SCHEDULE
        // =====================================================

        if (doctor.getStartTime() == null
                || doctor.getEndTime() == null
                || doctor.getSlotDuration() <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Doctor schedule is not configured."
                    );
        }


        // =====================================================
        // CONVERT TIMES
        // =====================================================

        LocalTime startTime;

        LocalTime endTime;

        try {

            startTime =
                    LocalTime.parse(
                            doctor.getStartTime()
                    );

            endTime =
                    LocalTime.parse(
                            doctor.getEndTime()
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Doctor schedule contains an invalid time."
                    );
        }


        // =====================================================
        // GENERATE ALL POSSIBLE SLOTS
        // =====================================================

        List<String> allSlots =
                new ArrayList<>();

        LocalTime currentTime =
                startTime;


        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "HH:mm"
                );


        while (
                currentTime.plusMinutes(
                        doctor.getSlotDuration()
                ).compareTo(endTime) <= 0
        ) {

            allSlots.add(
                    currentTime.format(formatter)
            );

            currentTime =
                    currentTime.plusMinutes(
                            doctor.getSlotDuration()
                    );
        }


        // =====================================================
        // REMOVE BOOKED SLOTS
        // =====================================================

        List<String> availableSlots =
                new ArrayList<>();


        for (String slot : allSlots) {

            boolean booked =
                    appointmentRepository
                            .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                                    doctorId,
                                    date,
                                    slot,
                                    "CANCELLED"
                            );

            if (!booked) {

                availableSlots.add(slot);
            }
        }


        // =====================================================
        // RETURN AVAILABLE SLOTS
        // =====================================================

        return ResponseEntity.ok(
                availableSlots
        );
    }
}