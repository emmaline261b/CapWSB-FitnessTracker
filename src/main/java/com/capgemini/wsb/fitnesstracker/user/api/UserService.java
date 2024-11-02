package com.capgemini.wsb.fitnesstracker.user.api;

import java.util.Optional;
import java.util.List;
/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User createUser(User user);
    Optional<User> getUserDetailsById(Long id);
    Optional<User> getUserDetailsByEmail(String email);
    User deleteUserById(Long id);

    List<User> findMatchingUsersByPartialEmail(String partialEmail);
}
