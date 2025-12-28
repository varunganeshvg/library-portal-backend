package com.example.bookservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bookservice.model.StaffAssignment;

public interface StaffAssignmentRepository extends JpaRepository<StaffAssignment, Long> {

    // get all assignments for one staff
    List<StaffAssignment> findByStaffId(Long staffId);

    // later useful for student-side filtering
    List<StaffAssignment> findByCourseAndSectionAndSemester(
            String course,
            String section,
            Integer semester
    );

    // ✅ NEW: for student dropdown (available classes)
    @Query("""
        SELECT DISTINCT sa.course, sa.section, sa.semester
        FROM StaffAssignment sa
    """)
    List<Object[]> findDistinctClasses();
}