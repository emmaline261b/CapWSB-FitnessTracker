package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link User} entities and data transfer objects (DTOs) such as {@link UserDto} and {@link UserSimpleDto}.
 */
@Component
public
class UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDto} with all fields populated.
     *
     * @param user the {@link User} entity to convert
     * @return a {@link UserDto} containing the full details of the user
     */
    protected UserDto toDto(User user) {
        return new UserDto(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdate(),
                user.getEmail());
    }

    /**
     * Converts a {@link User} entity to a {@link UserDto} containing only the user's ID and email.
     * Other fields in the DTO are set to null.
     *
     * @param user the {@link User} entity to convert
     * @return a {@link UserDto} with only the ID and email fields populated
     */
    protected UserDto toDtoJustEmailAndId(User user) {
        return new UserDto(user.getId(),
                null, null, null,
                user.getEmail());
    }

    /**
     * Converts a {@link User} entity to a {@link UserSimpleDto}, which contains only basic information:
     * ID, first name, and last name.
     *
     * @param user the {@link User} entity to convert
     * @return a {@link UserSimpleDto} containing the user's ID, first name, and last name
     */
    UserSimpleDto toSimpleDto(User user) {
        return new UserSimpleDto(user.getId(),
                user.getFirstName(),
                user.getLastName());
    }

    /**
     * Converts a {@link UserDto} to a {@link User} entity, mapping the basic user information.
     *
     * @param userDto the {@link UserDto} to convert
     * @return a {@link User} entity populated with data from the {@link UserDto}
     */
    protected User toEntity(UserDto userDto) {
        return new User(
                userDto.firstName(),
                userDto.lastName(),
                userDto.birthdate(),
                userDto.email());
    }

}