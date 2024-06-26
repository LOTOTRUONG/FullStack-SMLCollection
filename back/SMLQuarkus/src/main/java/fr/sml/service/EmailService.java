package fr.sml.service;

import fr.sml.service.email.Email;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@RegisterRestClient(configKey = "mail-service")
@Path("/email")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EmailService {
    @POST
    @Transactional
    Response sendEmail(Email email, @HeaderParam("ApiKey") String apiKey);
}
