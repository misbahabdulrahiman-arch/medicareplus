package com.medicare.medicareplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientManagementController {

    @GetMapping("/admin-patients")
    public String patients() {
        return "admin-patients";
    }
}