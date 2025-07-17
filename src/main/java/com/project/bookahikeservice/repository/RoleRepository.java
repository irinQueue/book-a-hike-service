package com.project.bookahikeservice.repository;

import com.project.bookahikeservice.dto.RoleUserCountProjection;
import com.project.bookahikeservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query(value = """
            SELECT r.name AS roleName,
                COUNT(ur.user_id) AS userCount
            FROM roles r
            LEFT JOIN
                user_roles ur ON r.id = ur.role_id
            GROUP BY
                r.name
            ORDER BY
                userCount DESC
            """, nativeQuery = true)
    List<RoleUserCountProjection> getUserCountsPerRole();

}
