package com.tectot.filebox.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {

    private Long id;

    @NotEmpty(message = "Name cannot be null")
    private String name;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password cannot be null")
    @NotEmpty
    private String password;

    @NotEmpty(message = "Organisation should not be empty")
    private Long organisationId;

    @NotEmpty(message = "Role cannot be null")
    @NotEmpty
    private String userRole;
}
