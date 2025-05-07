package sdu.codeeducationplat.dto.identity;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}