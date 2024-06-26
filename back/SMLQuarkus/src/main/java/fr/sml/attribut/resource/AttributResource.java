package fr.sml.attribut.resource;

import fr.sml.attribut.dto.AttributDto;
import fr.sml.attribut.entity.AttributEntity;
import fr.sml.attribut.repository.AttributRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/attributes")
@Tag(name = "5 - ATTRIBUTS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttributResource {
    @Context
    UriInfo uriInfo;
    @Inject
    AttributRepository attributRepository;

    @GET
    @Operation(summary = "GET ALL", description = "get all attributes")
    public Response getAll(){
        List<AttributEntity> attributsEntities = attributRepository.listAll();
        return Response.ok(AttributDto.toDtoList(attributsEntities)).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "ATTRIBUTE BY ID", description = "search attribute by its ID")
    public Response getById(@PathParam("id") Integer id) throws Exception {
        AttributEntity attributEntity = attributRepository.findById(id);
        if (attributEntity == null)
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        else {
            UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("/attributes");
            return Response.ok(new AttributDto(attributEntity, uriBuilder)).build();
        }
    }

    @Transactional
    @POST
    @Operation(summary = "INSERT", description = "add new attribute")
    public Response insert(@QueryParam("Name of Attribute") String name){
        AttributEntity attribute = new AttributEntity();
        if (name == null || name.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of attribute").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (attributRepository.findByName(name) != null)
            return Response.status(Response.Status.CONFLICT).entity("This name is duplicated").type(MediaType.TEXT_PLAIN_TYPE).build();
        attribute.setLibelleAttribut(name);
        attributRepository.persist(attribute);
        UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        uriBuilder.path(attribute.getIdAttribut().toString());
        return Response.created(uriBuilder.build()).entity("Insert successfully").type(MediaType.TEXT_PLAIN_TYPE).build();

    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Modify", description = "update the attribute")
    @Transactional
    public Response update(@PathParam("id") Integer id, @QueryParam("Name of Attribute") String name) {
        AttributEntity existingAttribute = attributRepository.findById(id);
        if (name == null || name.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of attribute").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (existingAttribute == null)
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (attributRepository.findByName(name) != null)
            return Response.status(Response.Status.CONFLICT).entity("This name is duplicated").type(MediaType.TEXT_PLAIN_TYPE).build();
        existingAttribute.setLibelleAttribut(name);
        attributRepository.persist(existingAttribute);
        return Response.ok(new AttributDto(existingAttribute)).build();
    }

    @Transactional
    @DELETE
    @Path("/{id}")
    @Operation(summary = "DELETE", description = "delete the attribute")
    public Response delete(@PathParam("id") Integer id) {
        if (id == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (attributRepository.deleteById(id)) {
            return Response.ok().entity("Successfully delete the attribute ID = " + id).type(MediaType.TEXT_PLAIN_TYPE).build();
        } else {
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }
}
