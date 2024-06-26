package fr.sml.attribut_type.resource;

import fr.sml.attribut_type.dto.AttributTypeDto;
import fr.sml.attribut_type.entity.AttributTypeEntity;
import fr.sml.attribut_type.repository.AttributTypeRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/attribute_Type")
@Tag(name = "4 - ATTRIBUTS DES TYPES")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttributTypeResource {
    @Context
    UriInfo uriInfo;
    @Inject
    AttributTypeRepository attributTypeRepository;

    @GET
    @Operation(summary = "GET ALL", description = "List all attributes of type")
    public Response getAll(){
        List<AttributTypeEntity> attributTypeEntities = attributTypeRepository.listAll();
        return Response.ok(AttributTypeDto.toDtoList(attributTypeEntities)).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "ATTRIBUTE OF TYPE BY ID", description = "Search attribute of type by its ID")
    public Response getById(@PathParam("id") Integer idAttributType){
        AttributTypeEntity attributType = attributTypeRepository.findById(idAttributType);
        if (attributType == null)
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        else
            return Response.status(200).entity(attributType).build();
    }

    @GET
    @Path("{idType}")
    @Operation(summary = "ATTRIBUTE OF TYPE BY ID TYPE", description = "Search attribute of type by id type")
    public Response getByIdType(@PathParam("idType") Integer idType){
        List<AttributTypeEntity> attributTypeEntities = attributTypeRepository.findByIdType(idType);
        return Response.ok(attributTypeEntities).build();
    }

}
