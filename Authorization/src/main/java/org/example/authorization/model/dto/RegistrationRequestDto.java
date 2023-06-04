package org.example.authorization.model.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String name;
    private String username;
    private String email;
    private String password;
}