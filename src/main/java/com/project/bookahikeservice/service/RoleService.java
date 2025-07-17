package com.project.bookahikeservice.service;

import com.project.bookahikeservice.dto.RoleUserCountProjection;

import java.util.List;

public interface RoleService {
    List<RoleUserCountProjection> getUserCountsPerRole();
}
