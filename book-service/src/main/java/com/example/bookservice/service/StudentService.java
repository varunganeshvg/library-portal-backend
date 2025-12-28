package com.example.bookservice.service;

import com.example.bookservice.model.StudentDetails;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.StudentDetailsRepository;
import com.example.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.bookservice.dto.ClassSubjectDto;
import com.example.bookservice.model.StaffAssignment;
import com.example.bookservice.model.StaffDetails;
import com.example.bookservice.repository.StaffAssignmentRepository;
import com.example.bookservice.repository.StaffDetailsRepository;



@Service
public class StudentService {

    private final StudentDetailsRepository studentDetailsRepository;
    private final BookRepository bookRepository;
    private final StaffAssignmentRepository staffAssignmentRepository;
    private final StaffDetailsRepository staffDetailsRepository;

    // 🧩 Constructor injection: Spring will provide these repositories automatically 
    public StudentService(StudentDetailsRepository studentDetailsRepository,
                          BookRepository bookRepository,
                          StaffAssignmentRepository staffAssignmentRepository,
                          StaffDetailsRepository staffDetailsRepository) {
        this.studentDetailsRepository = studentDetailsRepository;
        this.bookRepository = bookRepository;
        this.staffAssignmentRepository=staffAssignmentRepository;
        this.staffDetailsRepository=staffDetailsRepository;
    }

    // 1️ Save or update student profile
    public StudentDetails saveOrUpdateProfile(StudentDetails details) {
        return studentDetailsRepository.save(details);
    }

    // 2️ Get profile by email
    public StudentDetails getProfileByEmail(String email) {
        return studentDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
    }

    // 3️⃣ Get books for this student
    public List<Book> getBooksForStudent(String email) {
        // 1️⃣ Get student profile
        StudentDetails student = getProfileByEmail(email);

        // 2️⃣ Fetch only books for that student’s class
        return bookRepository.findByCourseAndSectionAndSemester(
                student.getCourse(),
                student.getSection(),
                student.getSemester()
        );
    }
    public List<StudentDetails> getAllStudents() {
        return studentDetailsRepository.findAll();
    }
    
    public List<ClassSubjectDto> getSubjectsForStudent(String email) {

        StudentDetails student = getProfileByEmail(email);

        List<StaffAssignment> assignments =
                staffAssignmentRepository.findByCourseAndSectionAndSemester(
                        student.getCourse(),
                        student.getSection(),
                        student.getSemester()
                );

        return assignments.stream().map(a -> {
            StaffDetails staff = staffDetailsRepository
                    .findByUserId(a.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found"));

            return new ClassSubjectDto(
                    a.getSubjectName(),
                    a.getStaffId(),          // ✅ AUTH userId
                    staff.getName()
            );
        }).toList();
    }
    
   
}