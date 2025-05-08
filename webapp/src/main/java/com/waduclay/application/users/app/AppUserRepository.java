package com.waduclay.application.users.app;

import com.waduclay.homefinder.shared.auth.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByBaseUser_Username(String baseUserUsername);

    List<AppUser> findAppUsersByBaseUser_Role(Role baseUserRole);
}
