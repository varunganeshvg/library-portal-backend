package com.example.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookservice.model.Book;
import java.util.List;



@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Staff-specific
    List<Book> findByCourseAndSectionAndSemesterAndUploadedByStaffId(
            String course,
            String section,
            Integer semester,
            Long uploadedByStaffId
    );

    // Student & Admin
    List<Book> findByCourseAndSectionAndSemester(
            String course,
            String section,
            Integer semester
    );
    
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(
            String title,
            String author,
            String category
    );

    List<Book> findByUploadedByStaffId(Long uploadedByStaffId);

    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByCategoryIgnoreCase(String category);
}
