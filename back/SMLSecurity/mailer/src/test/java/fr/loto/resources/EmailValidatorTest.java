package fr.loto.resources;
import fr.loto.util.EmailValidator;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest

public class EmailValidatorTest {

    @Test
    public void testValidEmail() {

        assertTrue(EmailValidator.isValidEmail("example@example.com"));
        assertTrue(EmailValidator.isValidEmail("user.name@example.com"));
        assertTrue(EmailValidator.isValidEmail("user_name@example.com"));
        assertTrue(EmailValidator.isValidEmail("user+name@example.com"));
        assertTrue(EmailValidator.isValidEmail("example123@example.co.uk"));
        assertTrue(EmailValidator.isValidEmail("pelovejeily@gmail.com"));
    }

    @Test
    public void testInvalidEmail() {
        assertFalse(EmailValidator.isValidEmail("example@example"));
        assertFalse(EmailValidator.isValidEmail("example.com"));
        assertFalse(EmailValidator.isValidEmail("example@.com"));
        assertFalse(EmailValidator.isValidEmail("example@com"));
        assertFalse(EmailValidator.isValidEmail("example@.co.uk"));
        assertFalse(EmailValidator.isValidEmail("example@co"));
        assertFalse(EmailValidator.isValidEmail("example@com."));
        assertFalse(EmailValidator.isValidEmail("example@-example.com"));
        assertFalse(EmailValidator.isValidEmail("example@exa_mple.com"));
        assertFalse(EmailValidator.isValidEmail("example@exa..mple.com"));
    }
}
