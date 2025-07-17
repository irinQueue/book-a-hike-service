package com.project.bookahikeservice.repository;

import com.project.bookahikeservice.dto.RoleUserCountDto;
import com.project.bookahikeservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("SELECT new com.project.bookahikeservice.dto.RoleUserCountDto(r.name, COUNT(u)) " +
            "FROM Role r LEFT JOIN r.name u GROUP BY r.name")
    List<RoleUserCountDto> getUserCountsPerRole();
}
