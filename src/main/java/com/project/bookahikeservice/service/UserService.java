package com.project.bookahikeservice.service;


import com.project.bookahikeservice.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User saveUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
}
