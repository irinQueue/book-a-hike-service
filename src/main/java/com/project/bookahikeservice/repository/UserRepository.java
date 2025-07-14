package com.project.bookahikeservice.repository;


import com.project.bookahikeservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}