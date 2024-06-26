package fr.loto.service;

import fr.loto.dto.Email;
import fr.loto.entities.UserEntity;
import fr.loto.repository.UserRepository;
import fr.loto.security.EncodeURL;
import fr.loto.security.HashPassword;
import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;
    @Inject
    Template EmailConfirmationResetTemplate;
    @Inject
    Template EmailRecoveryLoginTemplate;
    @Inject
    @RestClient
    EmailService emailService;
    private static final String apiKey = ConfigProvider.getConfig().getValue("api.key", String.class);
    private static String confirmationCode;
    private final UriInfo uriInfo;

    @Inject
    public UserService(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }


    @Transactional
    public void reset(UserEntity userEntity, String newPassword, String newEmail) throws Exception {
        LocalDateTime creationTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCreationTime = creationTime.format(formatter);
        if (newEmail == null || newEmail.isEmpty()){
            confirmationCode = userEntity.getLogin() + "/" + HashPassword.generate(newPassword) + "/" + userEntity.getEmail() + "/" + userEntity.getRoleUser() + "/" + formattedCreationTime;
        }
        else if (newPassword == null || newPassword.isEmpty()) {
            confirmationCode = userEntity.getLogin() + "/" + userEntity.getPassword() + "/" + newEmail + "/" + userEntity.getRoleUser() + "/" + formattedCreationTime;
        }
        String confirmationCodeEncrypted = EncodeURL.encrypt(confirmationCode);
        String confirmationCodeEncoder = Base64.getEncoder().encodeToString(confirmationCodeEncrypted.getBytes(StandardCharsets.UTF_8));
        System.out.println(confirmationCodeEncoder);
        sendConfirmationEmail(userEntity.getEmail(), userEntity.getLogin(), confirmationCodeEncoder.toString(), "/users/request_reset/confirm");
    }

    private void sendConfirmationEmail(String email, String username, String confirmationCode, String confirmEndpoint) {
        String confirmationUrl = uriInfo.getBaseUriBuilder().path(confirmEndpoint).queryParam("code", confirmationCode).build().toString();
        String html = EmailConfirmationResetTemplate.data("confirmationUrl", confirmationUrl, "destination", username).render();
        Email sendEmail = new Email(email, "Confirm Request Resetting", html);
        emailService.sendEmail(sendEmail,apiKey);

        System.out.println(confirmationUrl);
    }

    @Transactional
    public boolean confirmReset(String code) throws Exception{
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

        UserEntity user = userRepository.findByLogin(login);
        if (user != null && user.getRoleUser().equals(role)) {
            // Update the user's password and email
            user.setPassword(password);
            user.setEmail(email.toLowerCase());
            userRepository.persist(user);
            return true;
        }
        return true;
    }


    @Transactional
    public void recoveryLogin(UserEntity userEntity, String email){
        String login = userEntity.getLogin();
        String html = EmailRecoveryLoginTemplate.data( "destination", userEntity.getEmail(),"login", login).render();
        Email sendEmail = new Email(email,"Your login recovery request",html);
        emailService.sendEmail(sendEmail,apiKey);

    }

}

