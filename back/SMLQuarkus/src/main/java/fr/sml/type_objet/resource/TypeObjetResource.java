package fr.sml.type_objet.resource;

import fr.sml.type_objet.dto.TypeObjetDto;
import fr.sml.type_objet.entity.TypeObjetEntity;
import fr.sml.type_objet.repository.TypeObjetRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Path("types")
@Tag(name = "3 - TYPES DES OBJETS")
@Produces(MediaType.APPLICATION_JSON)
public class TypeObjetResource {
    @Context
    UriInfo uriInfo;
    @Inject
    TypeObjetRepository typeObjetRepository;

    @GET
    @Operation(summary = "GET ALL", description = "get all types object")
    public Response getAll(){
        List<TypeObjetEntity> typeObjetEntityList = typeObjetRepository.listAll();
        return Response.ok(TypeObjetDto.toDtoList(typeObjetEntityList)).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "TYPE BY ID", description = "search type by its ID")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Integer id) throws Exception {
        TypeObjetEntity typeObjetEntity = typeObjetRepository.findById(id);
        if (typeObjetEntity == null)
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        else {
            UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("/types");
            TypeObjetDto typeObjetDto = new TypeObjetDto(typeObjetEntity, uriBuilder);
            return Response.ok(typeObjetDto).build();
        }
    }

    @POST
    @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "CREATE", description = "add new type object")
    @Transactional
    public Response create(@FormParam("nomType") String typeObjetName){
        TypeObjetEntity typeObjet = new TypeObjetEntity();
        if (typeObjetName == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of type object").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (typeObjetRepository.findByName(typeObjetName)!= null)
            return Response.status(Response.Status.CONFLICT).entity("This name is duplicated").type(MediaType.TEXT_PLAIN_TYPE).build();
        typeObjet.setNomTypeObjet(typeObjetName.toLowerCase());
        typeObjetRepository.persist(typeObjet);
        return Response.status(201).build();
    }

    @DELETE
    @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "DELETE", description = "delete a type object")
    @Transactional
    public Response delete(@FormParam("nomType") String typeObjetName){
        TypeObjetEntity typeObjet = new TypeObjetEntity();
        if (typeObjetName == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of type object").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (typeObjetRepository.findByName(typeObjetName) == null)
            return Response.status(Response.Status.CONFLICT).entity("This name is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        typeObjet.setNomTypeObjet(typeObjetName.toLowerCase());
        typeObjetRepository.delete(typeObjet);
        return Response.status(204).build();
    }

    @PUT
    @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "UPDATE", description = "update a type object")
    @Transactional
    public Response update(@FormParam("nomType") String typeObjetName){
        TypeObjetEntity typeObjet = new TypeObjetEntity();
        if (typeObjetName == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter the name of type object").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (typeObjetRepository.findByName(typeObjetName) == null)
            return Response.status(Response.Status.CONFLICT).entity("This name is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        typeObjet.setNomTypeObjet(typeObjetName.toLowerCase());
        typeObjetRepository.persist(typeObjet);
        return Response.status(204).build();
    }


}

