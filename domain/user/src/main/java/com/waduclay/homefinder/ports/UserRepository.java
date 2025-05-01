package com.waduclay.homefinder.ports;

import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;


import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for the UserAggregate.
 * This interface follows the repository pattern and provides methods to
 * save, find, and delete UserAggregate instances.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface UserRepository {
    
    /**
     * Saves a UserAggregate to the repository.
     * 
     * @param user the UserAggregate to save
     * @return the saved UserAggregate
     */
    User save(User user);
    
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
     * Deletes a UserAggregate from the repository.
     * 
     * @param user the UserAggregate to delete
     */
    void delete(User user);
}
