package com.example.bookservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookservice.model.StaffDetails;

@Repository
public interface StaffDetailsRepository extends JpaRepository<StaffDetails, Long> {

    Optional<StaffDetails> findByEmail(String email);

    Optional<StaffDetails> findByUserId(Long userId); // 🔥 REQUIRED
}