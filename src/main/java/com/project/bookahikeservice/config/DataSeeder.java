package com.project.bookahikeservice.config;

import com.project.bookahikeservice.entity.Role;
import com.project.bookahikeservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            List<String> roles = List.of("ROLE_JOINER", "ROLE_ORGANIZER", "ROLE_COORDINATOR", "ROLE_ADMIN");

            for (String roleName : roles) {
                roleRepository.findByName(roleName).orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });
            }

            System.out.println(" Roles seeded: " + roleRepository.findAll().size());
        };
    }
}
