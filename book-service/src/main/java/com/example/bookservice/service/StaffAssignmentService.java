package com.example.bookservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.bookservice.dto.ClassOptionDto;
import com.example.bookservice.repository.StaffAssignmentRepository;

@Service
public class StaffAssignmentService {

    private final StaffAssignmentRepository staffAssignmentRepository;

    public StaffAssignmentService(StaffAssignmentRepository staffAssignmentRepository) {
        this.staffAssignmentRepository = staffAssignmentRepository;
    }

    // 🔹 Get all available classes (for students)
    public List<ClassOptionDto> getAvailableClasses() {

        return staffAssignmentRepository
                .findDistinctClasses()
                .stream()
                .map(obj -> new ClassOptionDto(
                        (String) obj[0],   // course
                        (String) obj[1],   // section
                        (Integer) obj[2]   // semester
                ))
                .collect(Collectors.toList());
    }
}