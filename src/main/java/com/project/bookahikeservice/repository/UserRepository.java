package com.project.bookahikeservice.repository;


import com.project.bookahikeservice.dto.UserResponseDto;
import com.project.bookahikeservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // <- add this method
    List<UserResponseDto> findAllProjectedBy();
}