package fr.loto.util;

import java.util.regex.Pattern;

public class InputValidator {
    //Validate email adresse format
    public static boolean isValidEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }
    //validate password stregth
    public static boolean isValidPassword(String password){
        return password.length() >= 8;
    }

}
