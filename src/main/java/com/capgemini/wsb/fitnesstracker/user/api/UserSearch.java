package com.capgemini.wsb.fitnesstracker.user.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserSearch {

    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String email;

}
