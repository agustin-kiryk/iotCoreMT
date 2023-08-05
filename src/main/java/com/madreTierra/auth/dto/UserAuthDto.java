package com.madreTierra.auth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserAuthDto {
    @Email(message = "Must be an valid email")
    private String email;
    @Size(min = 8)
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;


}

