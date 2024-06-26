package fr.loto.resources;

import fr.loto.dto.Email;
import fr.loto.dto.UserDto;
import fr.loto.entities.UserEntity;
import fr.loto.repository.UserRepository;
import fr.loto.service.EmailService;
import io.quarkus.qute.Template;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;
import java.util.stream.Collectors;


@Path("admin")
@Tag(name = "3 - Gestion des utilisateurs")
@DeclareRoles({"superadmin","admin","user"})
@ApplicationScoped
public class AdminResource {
    @Inject
    UserRepository userRepository;
    @Context
    UriInfo uriInfo;
    @Inject
    @RestClient
    EmailService emailService;
    private static final String apiKey = ConfigProvider.getConfig().getValue("api.key", String.class);
    @Inject
    Template EmailDeactiveTemplate;

    @Context
    SecurityContext securityContext;

    @GET
    @Path("allUsers")
    @Operation(summary = "All users", description = "List all users (for Admin only)")
    @RolesAllowed({"superadmin","admin"})
    @Produces(MediaType.APPLICATION_JSON)

    public Response getAllUsers() {
        List<UserEntity> usersEntities = userRepository.listAll();
        List<UserDto> userDtos = usersEntities.stream()
                .map(userEntity -> {
                    UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
                    boolean isAdmin = userEntity.getRoleUser().equalsIgnoreCase("admin");
                    try {
                        return new UserDto(userEntity, uriBuilder, isAdmin);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return Response.ok(userDtos).build();
    }

    @PUT
    @Path("/{login}/updateRole")
    @RolesAllowed({"superadmin","admin"})
    @Operation(summary = "CHANGE THE ROLE", description = "Update user role")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response changeRole(@PathParam("login") String login,  @FormParam("role") String role) {
        UserEntity userEntity = userRepository.findByLogin(login);
        if (login == null || login.isEmpty() || role == null || role.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Login or role is empty! Please insert value of login or role").build();
        }
        if (userEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        // Validate the role (only 'user' or 'admin' are allowed)
        if (!role.equalsIgnoreCase("user") && !role.equalsIgnoreCase("admin")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid role! Choose between user and admin").build();
        }
        // Update the user's role
        userEntity.setRoleUser(role.toLowerCase());
        userRepository.persist(userEntity);
        return Response.ok().entity("Role updated successfully").build();
    }

    @PUT
    @Operation(summary = "DEACTIVATE ACCOUNT", description = "desactivate account")
    @RolesAllowed({"superadmin","admin"})
    @Path("{login}/deactivate")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response deactivateAccount(@PathParam("login") String login) {
        UserEntity usersEntity = userRepository.findById(login);
        if (usersEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        usersEntity.setCodePin(null);
        usersEntity.setConnectionDate(null);
        usersEntity.setLoginAttempts(null);
        usersEntity.setStatus(false);
        userRepository.persist(usersEntity);
        String htmlMessage = EmailDeactiveTemplate.data("destination", usersEntity.getLogin(), "status", "DEACTIVATED").render();
        Email sendEmail = new Email(usersEntity.getEmail(), "Account Deactivated Message", htmlMessage);
        emailService.sendEmail(sendEmail, apiKey);

        return Response.ok().entity("Account deactivated successfully").build();
    }

    @PUT
    @Operation(summary = "RE-ACTIVATE ACCOUNT", description = "re-activate account")
    @RolesAllowed({"superadmin","admin"})
    @Path("{login}/reactivate")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response reactivateAccount(@PathParam("login") String login) {
        UserEntity usersEntity = userRepository.findById(login);
        if (usersEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        usersEntity.setStatus(true);
        usersEntity.setLoginAttempts(0);
        userRepository.persist(usersEntity);
        String htmlMessage = EmailDeactiveTemplate.data("destination", usersEntity.getLogin(), "status", "RE-ACTIVATED").render();
        Email sendEmail = new Email(usersEntity.getEmail(), "Account Re-activated Message", htmlMessage);
        emailService.sendEmail(sendEmail, apiKey);
        return Response.ok().entity("Account re-activated successfully").build();
    }


}

