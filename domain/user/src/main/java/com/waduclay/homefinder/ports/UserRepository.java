package com.waduclay.homefinder.ports;

import com.waduclay.homefinder.users.User;

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
     * Deletes a UserAggregate from the repository.
     *
     * @param user the UserAggregate to delete
     */
    void delete(User user);
}
