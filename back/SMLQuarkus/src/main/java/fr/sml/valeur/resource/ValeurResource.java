package fr.sml.valeur.resource;

import fr.sml.valeur.dto.ValeurDto;
import fr.sml.valeur.entity.ValeurEntity;
import fr.sml.valeur.repository.ValeurRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/values")
@Tag(name = "6 - VALEUR")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ValeurResource {
    @Context
    UriInfo uriInfo;
    @Inject
    ValeurRepository valeurRepository;

    @GET
    @Operation(summary = "GET ALL", description = "get all values")
    public Response getAll(){
        List<ValeurEntity> valeursEntities = valeurRepository.listAll();
        return Response.ok(ValeurDto.toDtoList(valeursEntities)).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "VALUE BY ID", description = "search value by its ID")
    public Response getById(@PathParam("id") Integer id){
        ValeurEntity valeurEntity = valeurRepository.findById(id);
        if (valeurEntity.getLibelleValeur()==null || valeurEntity.getLibelleValeur().isEmpty())
            valeurEntity.setLibelleValeur("null");
        if (valeurEntity == null)
            return Response.status(404).entity("ID is not existed").type(MediaType.TEXT_PLAIN_TYPE).build();
        else
            return Response.status(200).entity(valeurEntity).build();
    }

    @POST
    @Operation(summary = "INSERT", description = "Create a new value")
    public Response insert(@QueryParam("1-Type") String nomType, @QueryParam("2-AttributType") String nomAttributType, @QueryParam("3-Valeur") String nomValeur) {
        if (nomType == null || nomType.isEmpty() || nomAttributType == null || nomAttributType.isEmpty() || nomValeur == null || nomValeur.isEmpty())
            return Response.status(400).entity("Missing parameters").type(MediaType.TEXT_PLAIN_TYPE).build();
        return Response.status(500).build();
    }

}
