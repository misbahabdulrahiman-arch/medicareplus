package com.medicare.medicareplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctorDashboardController {

    @GetMapping("/doctor-dashboard")
    public String doctorDashboard() {

        return "doctor-dashboard";
    }
}