package com.example.authservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.authservice.model.User;

@Repository
public interface Userrepository extends JpaRepository<User, Long> {
  User findByEmail(String email);
}
