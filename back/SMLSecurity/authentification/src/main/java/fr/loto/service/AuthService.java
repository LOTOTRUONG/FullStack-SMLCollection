package fr.loto.service;
import fr.loto.dto.Email;
import fr.loto.dto.LoginDto;
import fr.loto.dto.NewUserDto;
import fr.loto.entities.UserEntity;
import fr.loto.repository.UserRepository;
import fr.loto.security.EncodeURL;
import fr.loto.security.HashPassword;
import fr.loto.util.CodepinGenerate;
import fr.loto.util.CodepinGenerate.PinCodeWithValidity;
import fr.loto.util.InputValidator;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@ApplicationScoped
public class AuthService {
    @Inject
    UserRepository userRepository;
    @Inject
    @RestClient
    EmailService emailService;
    @Inject
    Template EmailConfirmationTemplate;
    @Inject
    Template EmailPinCodeTemplate;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int BLOCK_DURATION_MINUTES = 10;
    private static final String apiKey = ConfigProvider.getConfig().getValue("api.key", String.class);
    private final UriInfo uriInfo;

    @Inject
    public AuthService(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Transactional
    public void createUser(NewUserDto newUserDto) throws Exception {
        LocalDateTime creationTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCreationTime = creationTime.format(formatter);
        String confirmationCode = newUserDto.getLogin() + "/" + HashPassword.generate(newUserDto.getPassword()) + "/" + newUserDto.getEmail() + "/" + "user" + "/" + formattedCreationTime;
        System.out.println(confirmationCode);
        String confirmationCodeEncrypted = EncodeURL.encrypt(confirmationCode);
        String confirmationCodeEncoder = Base64.getEncoder().encodeToString(confirmationCodeEncrypted.getBytes(StandardCharsets.UTF_8));
        System.out.println(confirmationCodeEncoder);
        sendConfirmationEmail(newUserDto.getEmail(), newUserDto.getLogin(), confirmationCodeEncoder.toString(),"/auth/register/confirm");
    }

    private void sendConfirmationEmail(String email, String username, String confirmationCode,String confirmEndpoint) {
        String confirmationUrl = uriInfo.getBaseUriBuilder().path(confirmEndpoint).queryParam("code", confirmationCode).build().toString();
        String html = EmailConfirmationTemplate.data("confirmationUrl", confirmationUrl, "destination", username).render();
        Email sendEmail= new Email(email, "Confirm Registration", html);
        emailService.sendEmail(sendEmail, apiKey);
        System.out.println(confirmationUrl);
    }


    @Transactional
    public boolean confirmRegistration(String code) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(code);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        System.out.println(decodedString);
        String decryptCode = EncodeURL.decrypt(decodedString);
        System.out.println(decryptCode);

        String[] parts = decryptCode.split("/");
        if (parts.length != 5) {
            return false;
        }
        String login = parts[0];
        System.out.println(login);
        String password = parts[1];
        System.out.println(password);
        String email = parts[2];
        System.out.println(email);
        String role = parts[3];
        System.out.println(role);
        //check if link has expired
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime creationTime = LocalDateTime.parse(parts[4],formatter);
        LocalDateTime now = LocalDateTime.now();
        long minutesElapsed = creationTime.until(now, ChronoUnit.MINUTES);
        if (minutesElapsed > 5){
            return false;
        }

        UserEntity user = new UserEntity();
        user.setLogin(login.toLowerCase());
        user.setPassword(password);
        user.setEmail(email.toLowerCase());
        user.setRoleUser(role.toLowerCase());
        userRepository.persist(user);

        return true; //successful registration
    }
    public boolean isLoginTaken(String login){
        return userRepository.findByLogin(login)!= null;
    }

    public boolean isValidPassword(String password){
        return InputValidator.isValidPassword(password);
    }

    public boolean isValidEmail(String email){
        return InputValidator.isValidEmail(email);
    }

    @Transactional
    public PinCodeWithValidity login(LoginDto loginDto) throws Exception {
        UserEntity user = userRepository.findByLogin(loginDto.getLogin());
        if (user == null)
            return null;
        if (canAttemptLogin(user) && user.getStatus()) {
            if (HashPassword.validate(loginDto.getPassword(), user.getPassword())) {
                resetLoginAttempts(user);
                PinCodeWithValidity pinCodeWithValidity = CodepinGenerate.generateCodePin();
                user.setCodePin(pinCodeWithValidity.getPinCode());
                user.setConnectionDate(LocalDateTime.now());
                userRepository.persist(user);
                String htmlMessage = EmailPinCodeTemplate.data("destination", user.getLogin(), "pincode", pinCodeWithValidity.getPinCode()).render();
                Email sendEmail = new Email(user.getEmail(), "Your Code Pin", htmlMessage);
                emailService.sendEmail(sendEmail, apiKey);
                return pinCodeWithValidity;
            } else {
                incrementLoginAttempts(user);
                if (user.getLoginAttempts() > MAX_LOGIN_ATTEMPTS) {
                    blockAccount(user);
                    return null; //Account blocked
                }
                return null; //wrong password
            }
        }
        else {
            if (user.getConnectionDate() != null &&
                    user.getConnectionDate().plusMinutes(BLOCK_DURATION_MINUTES).isBefore(LocalDateTime.now())) {
                user.setLoginAttempts(0);
                userRepository.persist(user);
            }
            return null; //wrong login}
        }

    }

    @Transactional
    public boolean validateCodePin(String login, Integer code) {
        UserEntity userEntity = userRepository.findByLogin(login);

        if (userEntity != null) {
            if (userEntity.getCodePin() != null && userEntity.getCodePin().equals(code)) {
                LocalDateTime creationDate = userEntity.getConnectionDate();
                LocalDateTime expirationTime = creationDate.plusMinutes(5);
                return LocalDateTime.now().isBefore(expirationTime);
            }
        }
        return false;
    }
    public String getRoleForLogin(String login) {
        UserEntity user = userRepository.findByLogin(login);
        if (user != null) {
            return user.getRoleUser();
        } else {
            return "user";

        }
    }

    private boolean canAttemptLogin(UserEntity user) {
        if (user.getLoginAttempts() != null) {
            return user.getLoginAttempts() <= MAX_LOGIN_ATTEMPTS;
        }
        return true;
    }


    private void incrementLoginAttempts(UserEntity user) {
        user.setLoginAttempts(user.getLoginAttempts() + 1);
        user.setConnectionDate(LocalDateTime.now());
        user.setCodePin(null);
        userRepository.persist(user);
    }

    private void resetLoginAttempts(UserEntity user) {
        user.setLoginAttempts(0);
        user.setConnectionDate(LocalDateTime.now());
        userRepository.persist(user);
    }

    private void blockAccount(UserEntity user) {
        LocalDateTime lastAttemptTime = user.getConnectionDate();
        if (lastAttemptTime != null && lastAttemptTime.plusMinutes(BLOCK_DURATION_MINUTES).isAfter(LocalDateTime.now())) {
            user.setCodePin(null);
            userRepository.persist(user);
        }
    }


}

