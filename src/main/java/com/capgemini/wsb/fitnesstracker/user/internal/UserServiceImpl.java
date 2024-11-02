package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private static final String NAME_REGEX = "^[\\p{L} .'-]+$";
    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private static final String PARTIAL_EMAIL_REGEX = "^[\\w.@-]+$";

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
        validateNewUser(user);

        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserDetailsById(Long id) {
        if (id == null || id < 1) {
            log.error("Invalid id");
            throw new IllegalArgumentException("Invalid id");
        }

        log.info("Getting details for user's id: {}", id);

        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserDetailsByEmail(String email) {
        if (null == email || email.isBlank()) {
            log.error("Invalid email");
            throw new IllegalArgumentException("Invalid email");
        }
        log.info("Getting details for user's email: {}", email);

        return userRepository.findByEmail(email);
    }
    @Override
    public User deleteUserById(Long id) {
        List<User> users = userRepository.findAllById(Collections.singleton(id));
        if (users.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        if (users.size() > 1) {
            throw new IllegalArgumentException("There is more than one user with id: " + id);
        }
        userRepository.delete(users.get(0));
        return users.get(0);
    }

    @Override
    public List<User> findMatchingUsers(UserSearch search) {
        validateSearch(search);

        log.info("Getting matching users by search: {}", search);
        return userRepository.findMatchingUser(search);
    }

    @Override
    public List<User> findMatchingUsersByPartialEmail(String partialEmail) {
        validatePartialEmail(partialEmail);
        log.info("Getting matching users by email fragment: {}", partialEmail);

        return userRepository.findAllByEmailContainingIgnoreCase(partialEmail);
    }

    @Override
    public List<User> findUsersOlderThan(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("The date is required.");
        }
        log.info("Getting users older than: {}", date);

        return userRepository.findByBirthdateBefore(date);
    }

    @Override
    public User updateUser(Long id, User userToUpdate) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validateUserToUpdate(userToUpdate);
        if (userToUpdate.getFirstName() != null) {
            existingUser.setFirstName(userToUpdate.getFirstName());
        }
        if (userToUpdate.getLastName() != null) {
            existingUser.setLastName(userToUpdate.getLastName());
        }
        if (userToUpdate.getBirthdate() != null) {
            existingUser.setBirthdate(userToUpdate.getBirthdate());
        }
        if (userToUpdate.getEmail() != null) {
            existingUser.setEmail(userToUpdate.getEmail());
        }

        return userRepository.save(existingUser);
    }


//==================== util methods ====================


    private void validateSearch(UserSearch search) {
        if (search.getFirstName() != null && !search.getFirstName().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid first name.");
        }

        if (search.getLastName() != null && !search.getLastName().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid last name");
        }

        if (search.getEmail() != null && !search.getEmail().matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email.");
        }

        if (search.getBirthdate() != null && search.getBirthdate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid Birthdate.");
        }
    }

    private void validateNewUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }

        if (!user.getFirstName().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("First name contains invalid characters.");
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required.");
        }
        if (!user.getLastName().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Last name contains invalid characters.");
        }

        if (user.getBirthdate() == null) {
            throw new IllegalArgumentException("Birthdate is required.");
        }
        if (user.getBirthdate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birthdate must be a date in the past.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!user.getEmail().matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private void validatePartialEmail(String partialEmail) {
        if (partialEmail == null || partialEmail.isBlank()) {
            throw new IllegalArgumentException("Email fragment is required.");
        }
        if (!partialEmail.matches(PARTIAL_EMAIL_REGEX)) {
            throw new IllegalArgumentException("Email fragment contains invalid characters.");
        }

    }

    private void validateUserToUpdate(User userToUpdate) {
        if (userToUpdate.getFirstName() != null && !userToUpdate.getFirstName().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid first name.");
        }

        if (userToUpdate.getLastName() != null && !userToUpdate.getLastName().matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid last name");
        }

        if (userToUpdate.getEmail() != null && !userToUpdate.getEmail().matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email.");
        }

        if (userToUpdate.getBirthdate() != null && userToUpdate.getBirthdate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid Birthdate.");
        }

    }

}