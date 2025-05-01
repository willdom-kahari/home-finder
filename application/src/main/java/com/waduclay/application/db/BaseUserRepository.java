package com.waduclay.application.db;

import com.waduclay.homefinder.shared.auth.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BaseUserRepository extends JpaRepository<BaseUser, UUID> {
    boolean existsByRole(Role role);

    boolean existsByUsername(String username);
}
