package fr.loto.resources;

import fr.loto.dto.EmailRecord;
import fr.loto.dto.KeyDto;
import fr.loto.entities.ClientEntity;
import fr.loto.entities.EmailEntity;
import fr.loto.repository.ClientRepository;
import fr.loto.repository.EmailRepository;
import fr.loto.util.InputValidator;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/apikeys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

@Tag(name = "2 - ApiKey")
public class ApiKeyResource {
    @Context
    UriInfo uriInfo;
    @Inject
    private ClientRepository clientRepository;
    @Inject
    private EmailRepository emailRepository;
    private static final Logger LOG = Logger.getLogger(ApiKeyResource.class.getName());


    @GET
    @Path("/{keys}")
    @Operation(summary = "CLIENTS BY APIKEY")
    public Response getClientByApiKey(@PathParam("keys") String keys){
        if (keys == null || keys.trim().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("ApiKey required").type(MediaType.TEXT_PLAIN).build();
        ClientEntity client = clientRepository.findByApiKey(keys);
        if (client == null)
            return Response.status(404, "Client not found").build();
        else {
            KeyDto keyDto = new KeyDto(client);
            return Response.ok(keyDto).build();
        }
    }
    @GET
    @Path("/{keys}/countEmail")
    @Operation(summary = "N° OF EMAIL BY APIKEY")
    public Response getCountEmail(@PathParam("keys") String keys){
        if (keys == null || keys.trim().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("ApiKey required").type(MediaType.TEXT_PLAIN).build();
        try {
            Long emailCountByMonth = emailRepository.countEmailByApiKey(keys);
            JsonObjectBuilder responseJsonBuilder = Json.createObjectBuilder();
            responseJsonBuilder.add("apikey", keys);
            responseJsonBuilder.add("emailCountByMonth", emailCountByMonth);
            responseJsonBuilder.add("Month-Year", (LocalDateTime.now().getMonthValue() + 1) + "-" + (LocalDateTime.now().getYear()));
            String responseJsonString = responseJsonBuilder.build().toString();
            return Response.ok(responseJsonString).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{keys}/email")
    @Operation(summary = "RECORD EMAIL", description = "Record an email for a client")
    @Transactional
    public Response saveMail(@PathParam("keys") String keys, EmailRecord emailRecord) {
        if (keys == null || keys.trim().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("ApiKey required").type(MediaType.TEXT_PLAIN).build();
        if (emailRecord.getSendTo() == null || emailRecord.getSendTo().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid email data provided").type(MediaType.TEXT_PLAIN).build();
        if (!InputValidator.isValidEmail(emailRecord.getSendTo()))
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid format email").type(MediaType.TEXT_PLAIN).build();

        try {
            ClientEntity clientEntity = clientRepository.findByApiKey(keys);
            Long emailCountByMonth = emailRepository.countEmailByApiKey(keys);
            if (clientEntity != null) {
                int maxEmailPerMonth = clientEntity.getMonthlyQuota();
                if (maxEmailPerMonth != 0 && emailCountByMonth >= maxEmailPerMonth)
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("Monthly email quota exceeded, cannot save the email").type(MediaType.TEXT_PLAIN).build();
                EmailEntity emailEntity = new EmailEntity();
                emailEntity.setSendTo(emailRecord.getSendTo());
                emailEntity.setSendAt(LocalDateTime.now());
                emailEntity.setSubject(emailRecord.getSubject());
                emailEntity.setClient(clientEntity);
                emailRepository.persist(emailEntity);
            }
            return Response.ok().entity("Success recording email").type(MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error recording email", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while recording the email").build();
        }

    }
}
