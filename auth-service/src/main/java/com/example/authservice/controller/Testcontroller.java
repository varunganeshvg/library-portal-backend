package com.example.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class Testcontroller {

    @GetMapping("/student")
    public String studentonly() {
        return "hello student";
    }

    @GetMapping("/staff")
    public String staffonly() {
        return "hello staff";
    }

    @GetMapping("/admin")
    public String adminonly() {
        return "hello admin";
    }
}
