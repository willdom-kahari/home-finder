package com.waduclay.homefinder.ports;


import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Query interface for the UserAggregate.
 * This interface follows the CQRS pattern by separating read operations from write operations.
 * It provides methods to query UserAggregate instances without modifying them.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface UserQuery {
    
    /**
     * Finds a UserAggregate by its ID.
     * 
     * @param id the ID of the UserAggregate to find
     * @return an Optional containing the UserAggregate if found, or empty if not found
     */
    Optional<User> findById(UUID id);
    
    /**
     * Finds a UserAggregate by its username.
     * 
     * @param username the username of the UserAggregate to find
     * @return an Optional containing the UserAggregate if found, or empty if not found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Finds all UserAggregates with the given role.
     * 
     * @param role the role to search for
     * @return a list of UserAggregates with the given role
     */
    List<User> findByRole(Role role);
    
    /**
     * Checks if a UserAggregate with the given username exists.
     * 
     * @param username the username to check
     * @return true if a UserAggregate with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Checks if a UserAggregate with the given role exists.
     * 
     * @param role the role to check
     * @return true if a UserAggregate with the given role exists, false otherwise
     */
    boolean existsByRole(Role role);
    
    /**
     * Counts the number of UserAggregates.
     * 
     * @return the number of UserAggregates
     */
    long count();
    
    /**
     * Finds all UserAggregates.
     * 
     * @return a list of all UserAggregates
     */
    List<User> findAll();
}
