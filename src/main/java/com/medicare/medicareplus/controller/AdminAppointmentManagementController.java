package com.medicare.medicareplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminAppointmentManagementController {

    @GetMapping("/admin-appointments")
    public String appointments() {
        return "admin-appointments";
    }
}