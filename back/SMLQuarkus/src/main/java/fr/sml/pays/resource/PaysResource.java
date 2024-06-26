package fr.sml.pays.resource;

import fr.sml.pays.dto.PaysDto;
import fr.sml.pays.entity.PaysEntity;
import fr.sml.pays.repository.PaysRepository;
import fr.sml.service.EmailService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;


@Path("/countries")
@Tag(name = "8 - PAYS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaysResource {
    @Context
    UriInfo uriInfo;
    @Inject
    PaysRepository paysRepository;

    @GET
    @Operation(summary = "GET ALL", description = "get all countries")
    public Response getAll(){
        List<PaysEntity> paysEntities = paysRepository.listAll();
        return Response.ok(PaysDto.toDtoList(paysEntities)).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "COUNTRY BY ID", description = "search pays by its ID")
    @APIResponse(responseCode = "200", description = "OK, Country found")
    @APIResponse(responseCode = "404", description = "Country not found")
    public Response getById(@PathParam("id") Integer id) throws Exception {
        PaysEntity pays = paysRepository.findById(id);
        if (pays == null)
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        else {
            UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("/countries");
            PaysDto paysDto = new PaysDto(pays,uriBuilder);
            return Response.ok(paysDto).build();
        }
    }

    @Transactional
    @POST
    @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "NEW", description = "add new country (for admin only)")
    public Response create(@FormParam("Name_Country") String countryName){
        PaysEntity pays = new PaysEntity();
        if (countryName == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of country").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (paysRepository.findByName(countryName.toUpperCase())!= null)
            return Response.status(Response.Status.CONFLICT).entity("This name is duplicated").type(MediaType.TEXT_PLAIN_TYPE).build();
        pays.setLibellePays(countryName.toUpperCase());
        paysRepository.persist(pays);
        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        uriBuilder.path(pays.getIdPays().toString());
        return Response.created(uriBuilder.build()).entity("Insert successfully").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "MODIFY", description = "update the country (only for admin)")
    @Transactional
    public Response update(@PathParam("id") Integer id, @FormParam("Name_Country") String nameCountry) {
        PaysEntity existingPays = paysRepository.findById(id);
        if (nameCountry == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of country").type(MediaType.TEXT_PLAIN_TYPE).build();
        // Retrieve the pays to be updated
        if (existingPays == null)
            return Response.status(Response.Status.NOT_FOUND).entity("ID is not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (paysRepository.findByName(nameCountry.toUpperCase())!= null)
            return Response.status(Response.Status.CONFLICT).entity("This name is duplicated").type(MediaType.TEXT_PLAIN_TYPE).build();
        existingPays.setLibellePays(nameCountry.toUpperCase());
        paysRepository.persist(existingPays);
        return Response.ok(existingPays).build();
    }

    @Transactional
    @DELETE
    @Path("{id}")
    @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "DELETE", description = "detele a country (only for admin)")

    public Response delete(@PathParam("id") Integer id) {
        if (id == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        PaysEntity paysEntity = paysRepository.findById(id);
        if (paysEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("ID is not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (paysRepository.deleteById(id)) {
            return Response.ok().entity("Successfully delete the country " + paysEntity.getLibellePays() + " (ID = " + id + ")").type(MediaType.TEXT_PLAIN_TYPE).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
