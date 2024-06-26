package fr.loto.security;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;
 @Singleton
public class TokenManager {
    public static String generateToken(String login, String role)  {
        return Jwt.issuer("jwt-token")
                .subject(login)
                .groups(role)
                .expiresIn(60*60*1)
                .sign();
    }



}
