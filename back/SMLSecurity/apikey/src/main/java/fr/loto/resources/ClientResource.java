package fr.loto.resources;

import fr.loto.dto.ClientDto;
import fr.loto.dto.Email;
import fr.loto.dto.NewClientDto;
import fr.loto.entities.ClientEntity;
import fr.loto.repository.ClientRepository;
import fr.loto.service.MailService;
import fr.loto.util.ApiKeyGenerator;
import io.quarkus.qute.Template;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "1 - Client")
public class ClientResource {
    @Context
    UriInfo uriInfo;
    @Inject
    ClientRepository clientRepository;


    @Inject
    @RestClient
    MailService mailService;

    public static final String apiKey = ConfigProvider.getConfig().getValue("api.key", String.class);

    private static final Integer DEFAULT_MONTHLY_QUOTA = 3;

    @Inject
    Template EmailApiNotificationTemplate;
    @Inject
    Template EmailClientNotificationTemplate;


    @GET
    @Operation(summary = "ALL CLIENTS", description = "List of all clients")
    @RolesAllowed("superadmin")

    public Response getAll() {
        List<ClientEntity> clientEntities = clientRepository.listAll();
        return Response.ok(ClientDto.toDtoList(clientEntities)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "CLIENT BY ID", description = "Search Client with the history of email by its ID")
    @RolesAllowed("superadmin")

    public Response getById(@PathParam("id") Integer id){
        ClientEntity client = clientRepository.findById(id);
        if (client == null)
            return Response.status(404, "ID is not existed").build();
        else {
            ClientDto clientDto = new ClientDto(client, true);
            return Response.ok(clientDto).build();
        }

    }

    @POST
    @Operation(summary = "NEW CLIENT", description = "Add new client")
    @RolesAllowed("superadmin")
    @Transactional
    public Response insert(NewClientDto newClientDto) {
        ClientEntity clientEntity = new ClientEntity();
        String generatedKey = ApiKeyGenerator.generateApiKey();
        clientEntity.setEmailClient(newClientDto.getEmailClient());
        clientEntity.setNomClient(newClientDto.getNomClient());
        clientEntity.setApiKey(generatedKey);
        Integer montthlyQuota =  newClientDto.getMonthlyQuota() != null ? newClientDto.getMonthlyQuota() : DEFAULT_MONTHLY_QUOTA;
        clientEntity.setMonthlyQuota(montthlyQuota);

        try {
            String emailHtml = EmailClientNotificationTemplate.data("clientName", newClientDto, "apikey", generatedKey).render();
            Email email = new Email(newClientDto.getEmailClient(), "Your API Key", emailHtml);
            mailService.sendEmail(email, apiKey);
            System.out.println(generatedKey);

            clientRepository.persist(clientEntity);

            if (clientEntity.getIdClient() != null) {
                UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
                uriBuilder.path(clientEntity.getIdClient().toString());
                return Response.created(uriBuilder.build()).entity("Successfully! Please check email to get your APIKEY").type(MediaType.TEXT_PLAIN_TYPE).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }


    @DELETE
    @Operation(summary = "DELETE", description = "delete a client")
    @Path("{id}")
    @Transactional
    @RolesAllowed("superadmin")
    public Response delete(@PathParam("id") Integer id){
        if (id == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        if (clientRepository.findById(id) == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if (clientRepository.deleteById(id))
            return Response.ok().entity("Success deleting the client ID " + id)
                    .type(MediaType.TEXT_PLAIN).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

    }

    @PUT
    @Operation(summary = "UPDATE QUOTA", description = "update the quota per month")
    @Path("{id}/quota")
    @Transactional
    public Response updateMonthlyQuota(@PathParam("id") Integer id, @QueryParam("quota") Integer newQuota){
        ClientEntity clientEntity = clientRepository.findById(id);
        if (clientEntity != null) {
            clientEntity.setMonthlyQuota(newQuota);
        }
        return Response.ok().entity("Success updating the monthly quota for client ID: " + id + ". New quota is " + newQuota)
                .type(MediaType.TEXT_PLAIN).build();
    }

    @PUT
    @Path("{id}/newKey")
    @Operation(summary = "UPDATE APIKEY", description = "Update new key for client")
    @Transactional
    @RolesAllowed("superadmin")
    public Response resetApiKey(@PathParam("id") Integer id){
        ClientEntity clientEntity = clientRepository.findById(id);
        String newApiKey = ApiKeyGenerator.generateApiKey();
        if (clientEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Client not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        else{
            clientEntity.setApiKey(newApiKey);
            String emailHtml = EmailApiNotificationTemplate.data("clientName", clientEntity.getNomClient(), "apikey", newApiKey).render();
            Email email = new Email(clientEntity.getEmailClient(), "Your New API Key", emailHtml);
            mailService.sendEmail(email, apiKey);

        }
        return Response.ok().entity("Success update APIKey for the client ID " + id)
                .type(MediaType.TEXT_PLAIN).build();
    }
}
