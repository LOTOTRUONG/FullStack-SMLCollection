package fr.loto.service;
import fr.loto.dto.Email;
import fr.loto.entities.ClientEntity;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@RegisterRestClient(configKey = "apikey-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/apikeys")
public interface ApiKeyService {
    @GET
    @Path("/{keys}")
    ClientEntity getClientByApiKey(@PathParam("keys") String keys);

    @GET
    @Path("/{keys}/countEmail")
    Response getCountEmail(@PathParam("keys") String keys);

    @POST
    @Transactional
    @Path("/{keys}/email")
    Response getSaveEmail(@PathParam("keys") String keys, Email email);

}
