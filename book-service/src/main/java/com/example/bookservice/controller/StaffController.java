package com.example.bookservice.controller;

import com.example.bookservice.model.StaffDetails;
import com.example.bookservice.model.Book;
import com.example.bookservice.model.StaffAssignment;
import com.example.bookservice.repository.StaffDetailsRepository;

import jakarta.servlet.http.HttpServletRequest;

import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.repository.StaffAssignmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


@RestController
@RequestMapping("/api/staff")
public class StaffController {
	
	@Value("${AUTH_SERVICE_URL}")
	private String authServiceUrl;

    @Autowired
    private StaffDetailsRepository staffDetailsRepository;

    @Autowired
    private BookRepository bookRepository;   // 🆕 to fetch books
    
    @Autowired
    private StaffAssignmentRepository staffAssignmentRepository;
    @GetMapping("/assignments")
    public List<StaffAssignment> getMyAssignments(Authentication authentication) {

        // Email from JWT
        String email = authentication.getName();

        // Find staff using email
        StaffDetails staff = staffDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        // Use AUTH userId
        Long userId = staff.getUserId();

        return staffAssignmentRepository.findByStaffId(userId);
    }
  

    @PostMapping("/add")
    public StaffDetails addStaff(
            @RequestBody StaffDetails staff,
            HttpServletRequest request
    ) {

        // 1️⃣ Read Authorization header from incoming request
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        // 2️⃣ Extract the JWT token (remove "Bearer ")
        String token = authHeader.substring(7);

        // 3️⃣ Prepare REST call to Auth Service
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 4️⃣ Call Auth Service to get userId by email
        String url = authServiceUrl + "/api/auth/user-id?email=" + staff.getEmail();

        ResponseEntity<Long> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Long.class
        );

        Long authUserId = response.getBody();

        if (authUserId == null) {
            throw new RuntimeException("Auth user not found for email: " + staff.getEmail());
        }

        // 5️⃣ Save staff with linked auth userId
        staff.setUserId(authUserId);
        return staffDetailsRepository.save(staff);
    }


    @GetMapping("/list")
    public List<StaffDetails> getAllStaff() {
        return staffDetailsRepository.findAll();
    }

    // 🆕 Get all books uploaded by a specific staff
    @GetMapping("/{staffId}/books")
    public List<Book> getBooksForStaff(@PathVariable Long staffId) {
        return bookRepository.findByUploadedByStaffId(staffId);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteStaff(@PathVariable Long id) {
        staffDetailsRepository.deleteById(id);
    }
    
    @PutMapping("/update/{id}")
    public StaffDetails updateStaff(
            @PathVariable Long id,
            @RequestBody StaffDetails updatedStaff) {

        StaffDetails existing = staffDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        existing.setName(updatedStaff.getName());
        existing.setDepartment(updatedStaff.getDepartment());
        existing.setStaffCode(updatedStaff.getStaffCode());

        return staffDetailsRepository.save(existing);
    }
    
    @GetMapping("/test-userid")
    public String testUserId(Authentication authentication) {

        Long userId = (Long) authentication.getDetails();

        System.out.println("✅ USER ID FROM JWT = " + userId);

        return "User ID = " + userId;
    }
    
    @GetMapping("/profile")
    public StaffDetails getStaffProfile(Authentication authentication) {

        String email = authentication.getName();

        return staffDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff profile not found"));
    }
    
    @GetMapping("/books/class")
    public List<Book> getBooksForStaffClass(
            @RequestParam String course,
            @RequestParam String section,
            @RequestParam Integer semester,
            Authentication authentication
    ) {
        String email = authentication.getName();

        StaffDetails staff = staffDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Long userId = staff.getUserId();

        return bookRepository
                .findByCourseAndSectionAndSemesterAndUploadedByStaffId(
                        course, section, semester, userId
                );
    }
}