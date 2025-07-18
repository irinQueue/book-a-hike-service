package com.project.bookahikeservice.service.impl;

import com.project.bookahikeservice.dto.projection.RoleUserCountProjection;
import com.project.bookahikeservice.repository.RoleRepository;
import com.project.bookahikeservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleUserCountProjection> getUserCountsPerRole() {
        return roleRepository.getUserCountsPerRole();
    }
}
