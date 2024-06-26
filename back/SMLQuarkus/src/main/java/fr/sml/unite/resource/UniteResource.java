package fr.sml.unite.resource;

import fr.sml.unite.dto.UniteDto;
import fr.sml.unite.entity.UniteEntity;
import fr.sml.unite.repository.UniteRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/units")
@Tag(name = "7 - UNITE")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UniteResource {
    @Inject
    private UniteRepository uniteRepository;
    @Context
    UriInfo uriInfo;

    @GET
    @Operation(summary = "GET ALL", description = "get all units")
    public Response getAll(){
        List<UniteEntity> unitesEntities = uniteRepository.listAll();
        return Response.ok(UniteDto.toDtoList(unitesEntities)).build();
    }
    @GET
    @Path("{id}")
    @Operation(summary = "UNIT BY ID", description = "search unit by its ID")
    public Response getById(@PathParam("id") Integer id) throws Exception {
        UniteEntity uniteEntity = uniteRepository.findById(id);
        if (uniteEntity == null)
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN).build();
        else {
            UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("/units");
            UniteDto uniteDto = new UniteDto(uniteEntity,uriBuilder);
            return Response.status(200).entity(uniteDto).build();
        }
    }

    @Transactional
    @POST
    @Operation(summary = "INSERT", description = "add new unit")
    @APIResponse(responseCode = "201", description = "Successfully inserting new unit")
    public Response insert(@QueryParam("Name Unit") String nameUnit){
        UniteEntity unite = new UniteEntity();
        if (nameUnit == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of unit").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (uniteRepository.findByName(nameUnit.toLowerCase())!= null)
            return Response.status(Response.Status.CONFLICT).entity("This name is duplicated").type(MediaType.TEXT_PLAIN_TYPE).build();
        unite.setLibelleUnite(nameUnit.toLowerCase());
        uniteRepository.persist(unite);
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        uriBuilder.path(unite.getIdUnite().toString());
        return Response.created(uriBuilder.build()).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Modify", description = "update the unit")
    @Transactional
    public Response update(@PathParam("id") Integer id, @QueryParam("Name_Unit") String nameUnit){
        UniteEntity existingUnite = uniteRepository.findById(id);
        if (nameUnit == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of unit").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (existingUnite == null)
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        // Retrieve the unit to be updated
        if (uniteRepository.findByName(nameUnit.toLowerCase())!= null)
            return Response.status(Response.Status.CONFLICT).entity("This name is duplicated").type(MediaType.TEXT_PLAIN_TYPE).build();
        existingUnite.setLibelleUnite(nameUnit.toLowerCase());
        uniteRepository.persist(existingUnite);
        return Response.ok(existingUnite).build();
    }

    @Transactional
    @DELETE
    @Path("/{id}")
    @Operation(summary = "DELETE", description = "delete the unit")
    public Response delete(@PathParam("id") Integer id) {
        if (id == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (uniteRepository.deleteById(id)) {
            return Response.ok().entity("Successfully delete the unit ID = " + id).type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
