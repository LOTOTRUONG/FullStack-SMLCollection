package fr.loto.resources;

import fr.loto.dto.UserDto;
import fr.loto.entities.UserEntity;
import fr.loto.repository.UserRepository;
import fr.loto.service.UserService;
import fr.loto.util.InputValidator;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("users")
@Tag(name = "2 - Gestion des comptes d'utilisateur")
@DeclareRoles({"superadmin","admin","user"})
public class UserResource {
    @Context
    UriInfo uriInfo;
    @Inject
    UserRepository userRepository;

    @Inject
    UserService userService;

    private UserDto userDto;
    @GET
    @Path("{login}")
    @Operation(summary = "USER BY LOGIN", description = "GET USER BY LOGIN (for all users)")
    @RolesAllowed({"user","admin","superadmin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByLogin(@PathParam("login") String login) throws Exception{
        UserEntity userEntity = userRepository.findByLogin(login);
        if (login == null || userEntity == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();

        if (userEntity.getRoleUser().equals("admin")||userEntity.getRoleUser().equals("superadmin")) {
            userDto = new UserDto(userEntity, uriBuilder,true);
        }
        else {
            userDto = new UserDto(userEntity, uriBuilder,false);
        }
        return Response.ok(userDto).build();
    }

    @POST
    @Path("{login}/request_password/")
    @Operation(summary = "REQUEST RESET PASSWORD", description = "MAKE A REQUEST FOR RESETTING PASSWORD (for all users)")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestPasswordReset(@PathParam("login") String login, @FormParam("NewPassword") String newPassword) throws Exception{
        UserEntity user = userRepository.findByLogin(login);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        if (!InputValidator.isValidPassword(newPassword))
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid format password")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        if (login == null || login.isEmpty() || newPassword == null || newPassword.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a login and a new password")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        userService.reset(user,newPassword,null);
        String message = "Please check your email to valid your account \nYour email is : " + user.getEmail();
        return Response.status(Response.Status.CREATED).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @GET
    @Path("/request_reset/confirm")
    @PermitAll
    @Operation(summary = "CONFIRM RESET", description = "CONFIRM RESET (for all users)")
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmResetRequest(@QueryParam("code") String code) throws Exception{
        if (userService.confirmReset(code)){
            return Response.ok("Changed successfully").build();
        }
        else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid or expired confirmation code")
                    .build();
        }
    }
    @POST
    @Path("{email}/request_login/")
    @Operation(summary = "REQUEST LOGIN RECOVERY", description = "MAKE A REQUEST FOR LOGIN RECOVERY (for all users)")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestLoginRecovery(@PathParam("email") String email){
        UserEntity user = userRepository.findByEmail(email);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Email not found")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        userService.recoveryLogin(user,email);
        String message = "Please check your email to find your login \nYour email is : " + user.getEmail();
        return Response.status(Response.Status.CREATED).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @POST
    @Path("{login}/request_email/")
    @Operation(summary = "REQUEST RESET EMAIL", description = "MAKE A REQUEST FOR RESETTING EMAIL (for all users)")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestEmailReset(@PathParam("login") String login, @FormParam("NewEmail") String newEmail) throws Exception{
        UserEntity user = userRepository.findByLogin(login);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        if (!InputValidator.isValidEmail(newEmail))
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid format email")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        if (login == null || login.isEmpty() || newEmail == null || newEmail.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a login and a new email address")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        userService.reset(user,null,newEmail);
        String message = "Please check your email to valid your account \nYour email is : " + user.getEmail();
        return Response.status(Response.Status.CREATED).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build();
    }




}

