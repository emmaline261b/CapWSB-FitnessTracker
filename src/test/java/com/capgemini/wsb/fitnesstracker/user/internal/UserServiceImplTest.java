package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserSearch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    @Test
    void findAllUsersShouldReturnListOfUsersWhenUsersExist() {
        // given
        User user1 = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        user1.setId(1L);
        User user2 = new User("Jane", "Smith", LocalDate.of(1990, 7, 20), "jane.smith@example.com");
        user1.setId(2L);
        List<User> users = List.of(user1, user2);
        when(mockUserRepository.findAll()).thenReturn(users);

        // when
        List<User> result = userService.findAllUsers();

        // then
        assertEquals(users.size(), result.size());
        assertEquals(users, result);
    }

    @Test
    void findAllUsersShouldReturnEmptyListWhenNoUsersExist() {
        // given
        when(mockUserRepository.findAll()).thenReturn(List.of());

        // when
        List<User> result = userService.findAllUsers();

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserDetailsByIdShouldReturnUserWhenUserExists() {
        // given
        Long validId = 1L;
        User user = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        user.setId(validId);
        when(mockUserRepository.findById(validId)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.getUserDetailsById(validId);

        // then
        assertEquals(Optional.of(user), result);
    }

    @Test
    void getUserDetailsByIdShouldReturnEmptyWhenUserDoesNotExist() {
        // given
        Long validId = 1L;
        when(mockUserRepository.findById(validId)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userService.getUserDetailsById(validId);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserDetailsByIdShouldThrowExceptionWhenIdIsNull() {
        // given
        Long invalidId = null;

        // when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.getUserDetailsById(invalidId));
        assertEquals("Invalid id", exception.getMessage());
    }

    @Test
    void getUserDetailsByIdShouldThrowExceptionWhenIdIsLessThanOne() {
        // given
        Long invalidId = 0L;

        // when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.getUserDetailsById(invalidId));
        assertEquals("Invalid id", exception.getMessage());
    }

    @Test
    void getUserDetailsByEmailShouldReturnUserWhenUserExists() {
        // given
        String validEmail = "john.doe@example.com";
        User user = new User("John", "Doe", LocalDate.of(1985, 5, 15), validEmail);
        user.setId(1L);
        when(mockUserRepository.findByEmail(validEmail)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.getUserDetailsByEmail(validEmail);

        // then
        assertEquals(Optional.of(user), result);
    }

    @Test
    void getUserDetailsByEmailShouldReturnEmptyWhenUserDoesNotExist() {
        // given
        String validEmail = "nonexistent@example.com";
        when(mockUserRepository.findByEmail(validEmail)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userService.getUserDetailsByEmail(validEmail);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    void getUserDetailsByEmailShouldThrowExceptionWhenEmailIsNull() {
        // given
        String invalidEmail = null;

        // when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.getUserDetailsByEmail(invalidEmail));
        assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    void getUserDetailsByEmailShouldThrowExceptionWhenEmailIsBlank() {
        // given
        String invalidEmail = "  ";

        // when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.getUserDetailsByEmail(invalidEmail));
        assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    void deleteUserByIdShouldDeleteUserWhenUserExists() {
        // given
        Long userId = 1L;
        User user = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        user.setId(userId);

        when(mockUserRepository.findAllById(Collections.singleton(userId))).thenReturn(List.of(user));

        // when
        User deletedUser = userService.deleteUserById(userId);

        // then
        assertEquals(user, deletedUser);
        verify(mockUserRepository, times(1)).delete(user);
    }

    @Test
    void deleteUserByIdShouldThrowExceptionWhenUserNotFound() {
        // given
        Long userId = 1L;
        when(mockUserRepository.findAllById(Collections.singleton(userId))).thenReturn(Collections.emptyList());

        // when and then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        assertEquals("User with ID=1 was not found", exception.getMessage());
    }

    @Test
    void deleteUserByIdShouldThrowExceptionWhenMultipleUsersWithSameId() {
        // given
        Long userId = 1L;
        User user1 = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        user1.setId(userId);
        User user2 = new User("Jane", "Doe", LocalDate.of(1990, 6, 20), "jane.doe@example.com");
        user2.setId(userId);

        when(mockUserRepository.findAllById(Collections.singleton(userId))).thenReturn(List.of(user1, user2));

        // when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(userId));
        assertEquals("There is more than one user with id: " + userId, exception.getMessage());
    }

    @Test
    void findMatchingUsersShouldReturnMatchingUsersWhenUsersExist() {
        // given
        UserSearch validSearch = new UserSearch("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        User user = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        user.setId(1L);

        when(mockUserRepository.findMatchingUser(validSearch)).thenReturn(List.of(user));

        // when
        List<User> result = userService.findMatchingUsers(validSearch);

        // then
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(mockUserRepository, times(1)).findMatchingUser(validSearch);
    }

    @Test
    void findMatchingUsersShouldReturnEmptyListWhenNoUsersMatch() {
        // given
        UserSearch validSearch = new UserSearch("Nonexistent", "User", LocalDate.of(1990, 1, 1), "nonexistent@example.com");
        when(mockUserRepository.findMatchingUser(validSearch)).thenReturn(Collections.emptyList());

        // when
        List<User> result = userService.findMatchingUsers(validSearch);

        // then
        assertTrue(result.isEmpty());
        verify(mockUserRepository, times(1)).findMatchingUser(validSearch);
    }

    @Test
    void findMatchingUsersShouldThrowExceptionWhenSearchCriteriaIsInvalid() {
        // given
        UserSearch invalidSearch = new UserSearch("!@#InvalidName", null, null, null);

        // when and then
        assertThrows(IllegalArgumentException.class, () -> userService.findMatchingUsers(invalidSearch));
        verify(mockUserRepository, never()).findMatchingUser(any());
    }

    @Test
    void findMatchingUsersByPartialEmailShouldReturnMatchingUsersWhenUsersExist() {
        // given
        String validPartialEmail = "doe";
        User user = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        user.setId(1L);

        when(mockUserRepository.findAllByEmailContainingIgnoreCase(validPartialEmail)).thenReturn(List.of(user));

        // when
        List<User> result = userService.findMatchingUsersByPartialEmail(validPartialEmail);

        // then
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(mockUserRepository, times(1)).findAllByEmailContainingIgnoreCase(validPartialEmail);
    }

    @Test
    void findMatchingUsersByPartialEmailShouldReturnEmptyListWhenNoUsersMatch() {
        // given
        String validPartialEmail = "unknown";
        when(mockUserRepository.findAllByEmailContainingIgnoreCase(validPartialEmail)).thenReturn(Collections.emptyList());

        // when
        List<User> result = userService.findMatchingUsersByPartialEmail(validPartialEmail);

        // then
        assertTrue(result.isEmpty());
        verify(mockUserRepository, times(1)).findAllByEmailContainingIgnoreCase(validPartialEmail);
    }

    @Test
    void findMatchingUsersByPartialEmailShouldThrowExceptionWhenEmailFragmentIsInvalid() {
        // given
        String invalidPartialEmail = "inv!@#$%lid";

        // when and then
        assertThrows(IllegalArgumentException.class, () -> userService.findMatchingUsersByPartialEmail(invalidPartialEmail));
        verify(mockUserRepository, never()).findAllByEmailContainingIgnoreCase(any());
    }

    @Test
    void findUsersOlderThanShouldReturnUsersWhenUsersExist() {
        // given
        LocalDate date = LocalDate.of(2000, 1, 1);
        User user = new User("John", "Doe", LocalDate.of(1990, 5, 15), "john.doe@example.com");
        user.setId(1L);

        when(mockUserRepository.findByBirthdateBefore(date)).thenReturn(List.of(user));

        // when
        List<User> result = userService.findUsersOlderThan(date);

        // then
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(mockUserRepository, times(1)).findByBirthdateBefore(date);
    }

    @Test
    void findUsersOlderThanShouldReturnEmptyListWhenNoUsersMatch() {
        // given
        LocalDate date = LocalDate.of(2000, 1, 1);
        when(mockUserRepository.findByBirthdateBefore(date)).thenReturn(Collections.emptyList());

        // when
        List<User> result = userService.findUsersOlderThan(date);

        // then
        assertTrue(result.isEmpty());
        verify(mockUserRepository, times(1)).findByBirthdateBefore(date);
    }

    @Test
    void findUsersOlderThanShouldThrowExceptionWhenDateIsNull() {
        // given
        LocalDate date = null;

        // when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.findUsersOlderThan(date));
        assertEquals("The date is required.", exception.getMessage());
        verify(mockUserRepository, never()).findByBirthdateBefore(any());
    }

    @Test
    void updateUserShouldUpdateUserWhenUserExists() {
        // given
        Long userId = 1L;
        User existingUser = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        existingUser.setId(userId);

        User userToUpdate = new User("Jonathan", "Smith", LocalDate.of(1985, 5, 15), "jon.smith@example.com");

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(mockUserRepository.save(existingUser)).thenReturn(existingUser);

        // when
        User updatedUser = userService.updateUser(userId, userToUpdate);

        // then
        assertEquals("Jonathan", updatedUser.getFirstName());
        assertEquals("Smith", updatedUser.getLastName());
        assertEquals("jon.smith@example.com", updatedUser.getEmail());
        verify(mockUserRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUserShouldThrowExceptionWhenUserNotFound() {
        // given
        Long userId = 1L;
        User userToUpdate = new User("Jonathan", "Smith", LocalDate.of(1985, 5, 15), "jon.smith@example.com");

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        // when and then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, userToUpdate));
        assertEquals("User with ID=1 was not found", exception.getMessage());
    }

    @Test
    void updateUserShouldThrowExceptionWhenUserToUpdateIsInvalid() {
        // given
        Long userId = 1L;
        User existingUser = new User("John", "Doe", LocalDate.of(1985, 5, 15), "john.doe@example.com");
        existingUser.setId(userId);

        User invalidUserToUpdate = new User("Invalid@Name", null, null, null);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, invalidUserToUpdate));
        assertEquals("Invalid first name.", exception.getMessage());
    }
}