package fr.loto.util;

import java.security.SecureRandom;

public class ApiKeyGenerator {
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Integer KEY_LENGTH = 16;

    private static final SecureRandom radom = new SecureRandom();

    public static String generateApiKey(){
        StringBuilder apiKeyBuilder = new StringBuilder();
        for (int i = 0; i < KEY_LENGTH; i++){
            int randomIndex = radom.nextInt(ALLOWED_CHARACTERS.length());
            apiKeyBuilder.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return apiKeyBuilder.toString();
    }
}
