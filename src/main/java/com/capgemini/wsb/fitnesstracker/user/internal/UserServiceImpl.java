package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
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
}