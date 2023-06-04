package tetris.model.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String name;
    private String username;
    private String email;
    private String password;

    public RegistrationRequestDto(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}