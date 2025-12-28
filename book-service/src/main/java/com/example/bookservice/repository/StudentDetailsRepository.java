package com.example.bookservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookservice.model.StudentDetails;

public interface StudentDetailsRepository extends JpaRepository<StudentDetails,Long> {

	Optional<StudentDetails>findByEmail(String email);
	
}
