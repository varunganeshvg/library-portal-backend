package com.example.bookservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookservice.model.Book;
import com.example.bookservice.model.StudentDetails;
//import com.example.bookservice.security.JwtUtil;
import com.example.bookservice.service.StudentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.core.Authentication;
import com.example.bookservice.service.StaffAssignmentService;
import com.example.bookservice.dto.ClassOptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.bookservice.dto.ClassSubjectDto;
import com.example.bookservice.repository.BookRepository;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class StudentController {

    private final StudentService studentService;
    private final StaffAssignmentService staffAssignmentService;
    private final BookRepository bookRepository;
    
    public StudentController(
            StudentService studentService,
            StaffAssignmentService staffAssignmentService,
            BookRepository bookRepository
    ) {
        this.studentService = studentService;
        this.staffAssignmentService = staffAssignmentService;
        this.bookRepository = bookRepository;
    }

    // 1️⃣ Create or update student profile (EMAIL FROM JWT ONLY)
    @PostMapping("/profile")
    public StudentDetails saveProfile(
            @RequestBody StudentDetails details,
            Authentication authentication) {

        // 🔐 Always trust JWT, never frontend
        String emailFromToken = authentication.getName();

        // Force correct email
        details.setEmail(emailFromToken);

        return studentService.saveOrUpdateProfile(details);
    }

    // 2️⃣ Get student profile (EMAIL FROM JWT)
    @GetMapping("/profile")
    public ResponseEntity<StudentDetails> getProfile(Authentication authentication) {

        String email = authentication.getName();

        try {
            StudentDetails details = studentService.getProfileByEmail(email);
            return ResponseEntity.ok(details);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    // 3️⃣ Get books for student (EMAIL FROM JWT)
    @GetMapping("/books")
    public List<Book> getBooks(Authentication authentication) {
        String email = authentication.getName();
        return studentService.getBooksForStudent(email);
    }
 // 🔹 Get available classes (for student dropdown)
    @GetMapping("/available-classes")
    public List<ClassOptionDto> getAvailableClasses() {
        return staffAssignmentService.getAvailableClasses();
    }
    
    @GetMapping("/subjects")
    public List<ClassSubjectDto> getSubjects(Authentication authentication) {
        String email = authentication.getName();
        return studentService.getSubjectsForStudent(email);
    }
    @GetMapping("/books/by-staff/{staffUserId}")
    public List<Book> getBooksByStaffForStudent(
            @PathVariable Long staffUserId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        StudentDetails student = studentService.getProfileByEmail(email);

        return bookRepository.findByCourseAndSectionAndSemesterAndUploadedByStaffId(
                student.getCourse(),
                student.getSection(),
                student.getSemester(),
                staffUserId
        );
    }
}