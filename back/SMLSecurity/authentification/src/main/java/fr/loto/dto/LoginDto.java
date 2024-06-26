package fr.loto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @Schema(description = "Login", required = true, example = "login")
    private String login;
    @Schema(description = "Password", required = true, example = "12345678")
    private String password;

    public LoginDto(String login){
        this.login = login;
    }

}
