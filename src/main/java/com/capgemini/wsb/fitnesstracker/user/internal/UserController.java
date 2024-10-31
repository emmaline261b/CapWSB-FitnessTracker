package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toDto)
                          .toList();
    }

    @GetMapping(value = "/simple")
    public List<UserSimpleDto> getSimpleDataForAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::toSimpleDto)
                          .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody UserDto userDto) {
        System.out.println("User with e-mail: " + userDto.email() + "passed to the request");

        return userService.createUser(userMapper.toEntity(userDto));
    }

    @GetMapping(value="/{id}")
    public UserDto getUserDetailsById(@PathVariable Long id) {
        return userService.getUserDetailsById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/email")
    public List<UserDto> getUserDetailsByEmail(@RequestParam String email) {
        return userService.getUserDetailsByEmail(email)
                .map(userMapper::toDto)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping(value = "/partial-email")
    public List<UserDto> getUserDetailsByPartialEmail(@RequestParam String partialEmail) {
        return userService.findMatchingUsersByPartialEmail(partialEmail)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}