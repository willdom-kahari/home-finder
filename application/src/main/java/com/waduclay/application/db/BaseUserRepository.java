package com.waduclay.application.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BaseUserRepository extends JpaRepository<BaseUser, UUID> {
}
