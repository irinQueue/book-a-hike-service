package com.project.bookahikeservice.service;

import com.project.bookahikeservice.dto.RoleUserCountDto;

import java.util.List;

public interface RoleService {
    List<RoleUserCountDto> getUserCountsPerRole();
}
