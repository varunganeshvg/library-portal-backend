package com.example.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.authservice.model.User;
import com.example.authservice.service.Userservice;
import com.example.authservice.jwt.JwtService;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
//@CrossOrigin(origins = { "http://127.0.0.1:5500","http://localhost:5500"})
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class Authcontroller {

    @Autowired
    private Userservice userservice;

    @Autowired
    private JwtService jwtService;   // 🆕 we inject JwtService here

    // ---------- Register ----------
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userservice.registerUser(user);
    }

    // ---------- Login ----------
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody User loginRequest) {

        // 1️⃣ Check email + password using Userservice
        User user = userservice.loginuser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        // 2️⃣ Generate JWT token for this user
        String token = jwtService.generateToken(
                user.getId(),       // 🔥 THIS is the key change
                user.getEmail(),
                user.getRole()
        );

        // 3️⃣ Build response JSON
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("name", user.getName());
        response.put("role", user.getRole());
        response.put("id", user.getId());   
        return response;
    }
    @GetMapping("/user-id")
    public Long getUserIdByEmail(@RequestParam String email) {
        User user = userservice.getUserByEmail(email);
        return user.getId();
    }
    
    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {
        String email = authentication.getName(); // from JWT
        return userservice.getUserByEmail(email);
    }
}
