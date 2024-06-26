package fr.loto.resources;

import fr.loto.dto.Email;
import fr.loto.entities.ClientEntity;
import fr.loto.repository.ClientRepository;
import fr.loto.service.ApiKeyService;
import fr.loto.util.EmailValidator;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.StringReader;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/email")
@Tag(name = "Email service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {
    @Inject
    @RestClient
    ApiKeyService apiKeyService;

    @Inject
    Mailer mailer;
    @Inject
    ClientRepository clientRepository;
    @Context
    UriInfo uriInfo;
    private static final Logger LOG = Logger.getLogger(EmailResource.class.getName());


    @POST
    @Transactional
    public Response sendEmail(Email email, @HeaderParam("ApiKey") String apiKey)  {
        if (apiKey==null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid api key provided").type(MediaType.TEXT_PLAIN).build();
        //input validation
        if (email.getSendTo()==null||email.getSendTo().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("email required").type(MediaType.TEXT_PLAIN).build();
        if (!EmailValidator.isValidEmail(email.getSendTo()))
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid email data provided").type(MediaType.TEXT_PLAIN).build();
        //client and quota validation
        try {
            ClientEntity clientEntity = clientRepository.findByApiKey(apiKey);
            if (clientEntity==null){
                return Response.status(400)
                                .entity("Invalid API key provided").type(MediaType.TEXT_PLAIN)
                                .build();
            }
            if (!isEmailCountValid(apiKey, clientEntity)){
                return Response.status(Response.Status.FORBIDDEN)
                                 .entity("Insufficient quota")
                                .build();
            }

            apiKeyService.getSaveEmail(apiKey,email);
            mailer.send(Mail.withHtml(email.getSendTo(), email.getSubject(), email.getText()));
            return Response.ok().entity("Success sending email to " + email.getSendTo()).type(MediaType.TEXT_PLAIN).build();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error sending email", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while sending the email").type(MediaType.TEXT_PLAIN).build();
        }


    }


    private boolean isEmailCountValid(String apikey, ClientEntity client){
        if (client.getMonthlyQuota() == 0)
            return true;
        Response countResponse = apiKeyService.getCountEmail(apikey);
        String jsonString = countResponse.readEntity(String.class);
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = jsonReader.readObject();
        Long emailCountByMonth = jsonObject.getJsonNumber("emailCountByMonth").longValue();
        return emailCountByMonth < client.getMonthlyQuota();
    }

}
