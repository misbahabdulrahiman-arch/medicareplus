package com.medicare.medicareplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.medicare.medicareplus.model.Doctor;
import com.medicare.medicareplus.repository.DoctorRepository;

@Controller
public class HomeController {

    @Autowired
    private DoctorRepository doctorRepository;


    // =====================================================
    // HOME PAGE
    // =====================================================

    @GetMapping("/")
    public String home() {

        return "index";
    }


    // =====================================================
    // DYNAMIC DOCTOR PROFILE
    // =====================================================

    @GetMapping("/doctor/{id}")
    public String doctorProfile(
            @PathVariable Long id,
            Model model) {

        Doctor doctor =
                doctorRepository
                        .findById(id)
                        .orElse(null);

        if (doctor == null) {

            return "redirect:/";
        }

        model.addAttribute(
                "doctor",
                doctor
        );

        return "doctor-profile";
    }


    // =====================================================
    // BOOKING
    // =====================================================

    @GetMapping("/booking")
    public String booking() {

        return "booking";
    }


    // =====================================================
    // APPOINTMENTS
    // =====================================================

    @GetMapping("/appointments")
    public String appointments() {

        return "appointments";
    }


    // =====================================================
    // REGISTER
    // =====================================================

    @GetMapping("/register")
    public String register() {

        return "register";
    }


    // =====================================================
    // LOGIN
    // =====================================================

    @GetMapping("/login")
    public String login() {

        return "login";
    }


    // =====================================================
    // DASHBOARD
    // =====================================================

    @GetMapping("/dashboard")
    public String dashboard() {

        return "dashboard";
    }


    // =====================================================
    // PROFILE
    // =====================================================

    @GetMapping("/profile")
    public String profile() {

        return "profile";
    }


    // =====================================================
    // MEDIBOT
    // =====================================================

    @GetMapping("/medibot")
    public String medibot() {

        return "medibot";
    }


    // =====================================================
    // MEDICAL RECORDS
    // =====================================================

    @GetMapping("/medical-records")
    public String medicalRecords() {

        return "medical-records";
    }


    // =====================================================
    // PRESCRIPTIONS
    // =====================================================

    @GetMapping("/prescriptions")
    public String prescriptions() {

        return "prescriptions";
    }


    // =====================================================
    // LAB REPORTS
    // =====================================================

    @GetMapping("/lab-reports")
    public String labReports() {

        return "lab-reports";
    }


    // =====================================================
    // HEALTH OVERVIEW
    // =====================================================

    @GetMapping("/health")
    public String health() {

        return "health";
    }


    // =====================================================
    // NOTIFICATIONS
    // =====================================================

    @GetMapping("/notifications")
    public String notifications() {

        return "notifications";
    }
    
    @GetMapping("/doctor-login")
    public String doctorLogin() {
        return "doctor-login";
    }
    
    @GetMapping("/emergency")
    public String emergency() {
        return "emergency";
    }

    @GetMapping("/consultation/{id}")
    public String consultation(@PathVariable Long id, Model model) {
        model.addAttribute("appointmentId", id);
        return "consultation";
    }
    
    @GetMapping("/consultation-room/{id}")
    public String consultationRoom(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("appointmentId", id);

        return "consultation-room";
    }
}