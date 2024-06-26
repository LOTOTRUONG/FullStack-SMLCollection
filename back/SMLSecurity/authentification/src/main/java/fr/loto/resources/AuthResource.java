package fr.loto.resources;

import fr.loto.dto.LoginDto;
import fr.loto.dto.NewUserDto;
import fr.loto.entities.UserEntity;
import fr.loto.repository.UserRepository;
import fr.loto.security.TokenManager;
import fr.loto.service.AuthService;
import fr.loto.util.CodepinGenerate.PinCodeWithValidity;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import vn.loto.HateOas;


import java.net.URI;
import java.time.format.DateTimeFormatter;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "1 - Gestion des comptes")
public class AuthResource {
    @Context
    UriInfo uriInfo;
    @Inject
    AuthService authService;

    @Inject
    UserRepository userRepository;

    @POST
    @PermitAll
    @Path("register")
    @Transactional
    @Operation(summary = "USER INSCRIPTION", description = "Demande d'inscription")
    public Response register(NewUserDto newUser) throws Exception {
      if (authService.isLoginTaken(newUser.getLogin())) {
          return Response.status(Response.Status.BAD_REQUEST)
               .entity("Login already taken")
               .build();
      }
      if (!authService.isValidPassword(newUser.getPassword())) {
          return Response.status(Response.Status.BAD_REQUEST)
             .entity("Invalid password")
             .build();
      }
      if (!authService.isValidEmail(newUser.getEmail())) {
          return Response.status(Response.Status.BAD_REQUEST)
           .entity("Invalid email")
           .build();
      }
        authService.createUser(newUser);
        String message = "Please check your email to valid your account \nYour email is : " + newUser.getEmail();
        return Response.status(Response.Status.CREATED).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build();
    }


    @GET
    @Path("register/confirm")
    @PermitAll
    @Operation(summary = "Confirm Registration", description = "Confirm user registration after email verification")
    public Response confirmRegistration(@QueryParam("code") String code) throws Exception {
        if (authService.confirmRegistration(code)) {
            return Response.ok("Registration confirmed successfully").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid or expired confirmation code")
                    .build();
        }
    }

    @POST
    @Path("login")
    @PermitAll
    @Transactional
    public Response login(LoginDto loginDto ) throws Exception {
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        HateOas hateOas = new HateOas();

        if (loginDto.getLogin() == null || loginDto.getLogin().isEmpty() || loginDto.getPassword() == null || loginDto.getPassword().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter login and password").type(MediaType.TEXT_PLAIN_TYPE).build();

        PinCodeWithValidity pinCode = authService.login(loginDto);
        UserEntity user = userRepository.findByLogin(loginDto.getLogin());
        if (user == null){
            hateOas.addLink("register", HttpMethod.POST, new URI(uriBuilder.clone().path("auth").path("register").build().toString()));
            hateOas.addLink("forgot login", HttpMethod.POST, new URI(uriBuilder.clone().path("users").path("enterYourEmail").path("forgot_login").build().toString()));
            return Response.ok(hateOas).status(Response.Status.NOT_FOUND).build();
        }
        if (user.getStatus() == false && user.getLoginAttempts() == null)
            return Response.status(Response.Status.FORBIDDEN).entity("Your account has been deactivated").type(MediaType.TEXT_PLAIN_TYPE).build();

        if (pinCode != null) {
            System.out.println(pinCode);
            return Response.ok().entity("PIN code sent: " + pinCode.getPinCode()).type(MediaType.TEXT_PLAIN_TYPE).build();
        } else {
            if (user.getLoginAttempts() != null && user.getLoginAttempts() >= 4) {
                if (user.getConnectionDate() != null) {
                    return Response.status(Response.Status.FORBIDDEN)
                        .entity("Your account has been blocked in 10 minutes from " + user.getConnectionDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " of the day " + user.getConnectionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .type(MediaType.TEXT_PLAIN_TYPE).build();
                }
            } else {
                hateOas.addLink("reset password", HttpMethod.POST, new URI(uriBuilder.clone().path("users").path(user.getLogin()).path("request_password").build().toString()));
                return Response.ok(hateOas).status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();

    }

    @POST
    @Path("{login}/codepin")
    @Transactional
    public Response codepin(@PathParam("login") String login, @QueryParam("CodePin") Integer code ) {
        boolean isValidCodePin = authService.validateCodePin(login,code);

        if (isValidCodePin) {
            String role = authService.getRoleForLogin(login);
            String token = TokenManager.generateToken(login,role);// Implement this method to generate token
            System.out.println(token);
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Code Pin ou Wrong Login").build();
        }
    }



}
