package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void createUserShouldSaveUserWhenCorrectValues() {
        //given
        User user = new User("Michael", "Jackson", LocalDate.of(1958,8,29), "michael.jackson@neverland.tx");
        when(mockUserRepository.save(user)).thenReturn(user);
        //when
        User createdUser = userService.createUser(user);

        //then
        assertEquals(user, createdUser);
    }

    @Test
    void createUserShouldThrowExceptionWhenNoFirstName() {
        //given
        User user = new User(null, "Stone", LocalDate.of(1990, 1, 1), "emma.stone@hollywood.ca");

        //when
        //then
            Throwable thrown = assertThrows(IllegalArgumentException.class,
                    () -> {
                        userService.createUser(user);
                    });
            assertEquals("First name is required.", thrown.getMessage());
    }

    @Test
    void createUserShouldThrowExceptionWhenInvalidCharacters() {
        //given
        User user = new User("!@#$%%", "Stone", LocalDate.of(1990, 1, 1), "emma.stone@hollywood.ca");

        //when
        //then
            Throwable thrown = assertThrows(IllegalArgumentException.class,
                    () -> {
                        userService.createUser(user);
                    });
            assertEquals("First name contains invalid characters.", thrown.getMessage());
    }

    @Test
    void getUserShouldReturnUserWhenUserExists() {
        // given
        Long userId = 1L;
        User user = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        user.setId(userId);
        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.getUser(userId);

        // then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void getUserShouldReturnEmptyWhenUserDoesNotExist() {
        // given
        Long userId = 1L;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userService.getUser(userId);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    void getUserByEmailShouldReturnUserWhenUserExists() {
        // given
        String email = "john.doe@example.com";
        User user = new User("John", "Doe", LocalDate.of(1985, 5, 15), email);
        user.setId(1L);
        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.getUserByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void getUserByEmailShouldReturnEmptyWhenUserDoesNotExist() {
        // given
        String email = "john.doe@example.com";
        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userService.getUserByEmail(email);

        // then
        assertFalse(result.isPresent());
    }

}