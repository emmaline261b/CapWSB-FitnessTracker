package com.capgemini.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User createUser(User user);
    Optional<User> getUserDetailsById(Long id);
    Optional<User> getUserDetailsByEmail(String email);
    List<User> findMatchingUsers(UserSearch search);
    List<User> findMatchingUsersByPartialEmail(String partialEmail);
    List<User> findUsersOlderThan(LocalDate date);
    User updateUser(Long id, User user);

    User deleteUserById(Long id);

}
