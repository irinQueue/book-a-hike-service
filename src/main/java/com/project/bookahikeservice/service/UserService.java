package com.project.bookahikeservice.service;


import com.project.bookahikeservice.dto.request.UserRegistrationDto;
import com.project.bookahikeservice.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User saveUser(UserRegistrationDto dto);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createOrganizer(UserRegistrationDto dto);

}
