package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * REST controller for managing user-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting users.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    /**
     * Retrieves all users and returns them as a list of UserDto objects.
     *
     * @return a list of all users in the system
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDto)
                          .toList();
    }

    /**
     * Retrieves simplified data for all users.
     *
     * @return a list of UserSimpleDto with basic user data
     */
    @GetMapping(value = "/simple", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserSimpleDto> getSimpleDataForAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toSimpleDto)
                          .toList();
    }

    /**
     * Adds a new user based on the provided UserDto data.
     *
     * @param userDto the user data transfer object with information to create a new user
     * @return the newly created User entity
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody UserDto userDto) {
        System.out.println("User with e-mail: " + userDto.email() + "passed to the request");

        return userService.createUser(userMapper.toEntity(userDto));
    }

    /**
     * Retrieves detailed information of a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the UserDto containing the user's details
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUserDetailsById(@PathVariable Long id) {
        return userService.getUserDetailsById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Retrieves detailed information of a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return a list containing the UserDto if found, or an empty list if not found
     */
    @GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getUserDetailsByEmail(@RequestParam String email) {
        return userService.getUserDetailsByEmail(email)
                .map(userMapper::toDto)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return the deleted User entity
     */
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    /**
     * Retrieves users whose email partially matches the provided string.
     *
     * @param partialEmail a partial email string to search for matching users
     * @return a list of UserDto objects of users with emails that contain the partial email
     */
    @GetMapping(value = "/partial-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getUserDetailsByPartialEmail(@RequestParam String partialEmail) {
        return userService.findMatchingUsersByPartialEmail(partialEmail)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves users who are older than the specified date.
     *
     * @param date the cutoff birthdate; users born before this date are returned
     * @return a list of UserDto objects of users older than the specified date
     */
    @GetMapping(value = "/older/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getUsersOlderThan(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return userService.findUsersOlderThan(date)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Searches for users matching specific criteria.
     *
     * @param userSearch the search criteria encapsulated in a UserSearch object
     * @return a list of UserDto objects that match the search criteria
     */
    @PostMapping(value = "/matching-users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> findMatchingUser(@RequestBody UserSearch userSearch) {
        return userService.findMatchingUsers(userSearch)
                .stream().map(userMapper::toDtoJustEmailAndId)
                .collect(Collectors.toList());
    }

    /**
     * Updates a user with the provided ID using the data from UserDto.
     *
     * @param id the ID of the user to update
     * @param userDto the new user data to update the existing user
     * @return the updated User entity
     */
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userMapper.toEntity(userDto));
    }
}