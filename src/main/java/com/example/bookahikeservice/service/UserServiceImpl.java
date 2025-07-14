package com.example.bookahikeservice.service;

import com.example.bookahikeservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public List<User> getUser() {
        return List.of(
                new User(1L, "Johnico", "Irinco", "irincojohnico@gmail.com","09123424242"),
                new User(2L, "Jane Doe", "Doe","jane.doe@example.com","09123424211")
        );
    }
}

