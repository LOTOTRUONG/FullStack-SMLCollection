package fr.loto.dto;

import fr.loto.entities.UserEntity;
import fr.loto.security.HashPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserDto {
    @Schema(description = "Email", required = true, example = "test1@gmail.com")
    private String email;
    @Schema(description = "Login", required = true, example = "login")
    private String login;
    @Schema(description = "Password", required = true, example = "12345678")
    private String password;


}
