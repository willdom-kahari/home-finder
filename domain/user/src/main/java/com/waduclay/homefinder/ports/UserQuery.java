package com.waduclay.homefinder.ports;


import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Query interface for the User.
 * This interface follows the CQRS pattern by separating read operations from write operations.
 * It provides methods to query User instances without modifying them.
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface UserQuery {

    /**
     * Finds a User by its ID.
     *
     * @param id the ID of the User to find
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findById(UUID id);

    Optional<User> findAuthUserByName(String name);

    /**
     * Finds a User by its username.
     *
     * @param username the username of the User to find
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds all Users with the given role.
     *
     * @param role the role to search for
     * @return a list of Users with the given role
     */
    List<User> findByRole(Role role);

    /**
     * Checks if a User with the given username exists.
     *
     * @param username the username to check
     * @return true if a User with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a User with the given role exists.
     *
     * @param role the role to check
     * @return true if a User with the given role exists, false otherwise
     */
    boolean existsByRole(Role role);


    /**
     * Finds all Users.
     *
     * @return a list of all Users
     */
    List<User> findAll();
}
