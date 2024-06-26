package fr.sml.attribut_objet.resource;

import fr.sml.attribut_objet.dto.NewAttributObjetDto;
import fr.sml.attribut_objet.dto.ViewObjetDto;
import fr.sml.attribut_objet.entity.ViewObjetEntity;
import fr.sml.attribut_objet.repository.AttributObjetRepository;
import fr.sml.attribut_objet.repository.ViewObjetRepository;
import fr.sml.attribut_type.entity.AttributTypeEntity;
import fr.sml.attribut_type.repository.AttributTypeRepository;
import fr.sml.objet.entity.ObjetEntity;
import fr.sml.objet.repository.ObjetRepository;
import fr.sml.users.repository.UserRepository;
import fr.sml.valeur.entity.ValeurEntity;
import fr.sml.valeur.repository.ValeurRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/attribute_object")
@Tag(name = "2 - ATTRIBUTS DES OBJETS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttributObjetResource {
    @Context
    UriInfo uriInfo;
    @Inject
    private AttributObjetRepository attributObjetRepository;
    @Inject
    ViewObjetRepository viewObjetRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    AttributTypeRepository attributTypeRepository;
    @Inject
    ObjetRepository objetRepository;

    @Inject
    EntityManager entityManager;
    @GET
    public Response getAll(){
        List<ViewObjetEntity> viewObjectEntities = viewObjetRepository.listAll();
        return Response.ok(ViewObjetDto.toDtoList(viewObjectEntities)).build();
    }

    @GET
    @Path("{login}")
    @Operation(summary = "ATTRIBUT OBJET BY LOGIN", description = "find the atttribute object by user's login")
    public Response getByIdType(@PathParam("login") String login){
        if (userRepository.findById(login) == null)
            return Response.status(404).entity("login not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        List<ViewObjetEntity> viewObjectEntities = viewObjetRepository.findByLogin(login);
        if (login == null)
            return Response.status(404).entity("login required").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (viewObjectEntities.isEmpty() || viewObjectEntities.size() == 0)
            return Response.status(404).entity("empty list").type(MediaType.TEXT_PLAIN_TYPE).build();
        return Response.ok(viewObjectEntities).build();
    }

    @GET
    @Path("{login}/{idType}/{idObject}")
    @Operation(summary = "ATTRIBUT OBJET BY LOGIN", description = "find the atttribute object by user's login")
    public Response getByIdType(@PathParam("login") String login, @PathParam("idType") Integer idType, @PathParam("idObject") Integer idObject){
        if (userRepository.findById(login) == null)
            return Response.status(404).entity("login not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        List<ViewObjetEntity> viewObjectEntities = viewObjetRepository.findDetailObject(login, idType, idObject);
        if (login == null)
            return Response.status(404).entity("login required").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (viewObjectEntities.isEmpty() || viewObjectEntities.size() == 0)
            return Response.status(404).entity("empty list").type(MediaType.TEXT_PLAIN_TYPE).build();
        return Response.ok(viewObjectEntities).build();
    }

    @POST
    @Operation(summary = "NEW", description = "Create a new attribute of object")
    @Path("{login}")
    @Transactional
    public Response create(@PathParam("login") String login, NewAttributObjetDto newAttributObjet){
        //check if user is exist
        if (userRepository.findById(login) == null)
            return Response.status(404).entity("login not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        //check if objet exist
        ObjetEntity existingObject = objetRepository.findByName(newAttributObjet.getNomObject());
        if (existingObject == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Object not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (!existingObject.getUser().getLogin().equals(login))
            return Response.status(Response.Status.FORBIDDEN).entity("You are not the owner of this object").type(MediaType.TEXT_PLAIN_TYPE).build();
        //check if attribut type exist
        AttributTypeEntity existingAttributType = attributTypeRepository.findByIdAttributType(newAttributObjet.getIdAttributType());
        if (existingAttributType == null)
            return Response.status(Response.Status.NOT_FOUND).entity("attribut type not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        if(attributObjetRepository.findByIdAttributType(newAttributObjet.getIdAttributType()) != null)
            return Response.status(Response.Status.CONFLICT).entity("Duplicate parameter").type(MediaType.TEXT_PLAIN_TYPE).build();
        // If the attribute type does not allow a string value but one is provided, return an error
        if (existingAttributType.isCommentaire() == false && newAttributObjet.getValeurString() != null)
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("FORBIDDEN TO INSERT VALEURSTRING").type(MediaType.TEXT_PLAIN_TYPE).build();
        if ((existingAttributType.isCommentaire() == true || existingAttributType.isTableDeDonnee() == true) && newAttributObjet.getValeurFloat() != null)
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity("FORBIDDEN TO INSERT VALEURFLOAT").type(MediaType.TEXT_PLAIN).build();

        entityManager.createNamedStoredProcedureQuery("InsertNewAttributeObject")
                .setParameter("nomType",existingAttributType.getTypeObjet().getNomTypeObjet())
                .setParameter("login",login)
                .setParameter("nomObjet",newAttributObjet.getNomObject())
                .setParameter("valeurFloat", newAttributObjet.getValeurFloat())
                .setParameter("valeurString",newAttributObjet.getValeurString())
                .setParameter("idAttributType", newAttributObjet.getIdAttributType())
                .setParameter("nomValeur", newAttributObjet.getNomValeur())
                .execute();
        return Response.ok().build();
    }

}
