package fr.sml.objet.resource;
import fr.sml.objet.dto.ObjetDto;
import fr.sml.objet.entity.ObjetEntity;
import fr.sml.objet.repository.ObjetRepository;
import fr.sml.pays.entity.PaysEntity;
import fr.sml.pays.repository.PaysRepository;
import fr.sml.type_objet.entity.TypeObjetEntity;
import fr.sml.type_objet.repository.TypeObjetRepository;
import fr.sml.users.repository.UserRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/objects")
@Tag(name = "1 - OBJET")
@Produces(MediaType.APPLICATION_JSON)
public class ObjetResource {
    @Context
    UriInfo uriInfo;
    @Inject
    private ObjetRepository objetRepository;
    @Inject
    PaysRepository paysRepository;
    @Inject
    TypeObjetRepository typeObjetRepository;
    @Inject
    UserRepository userRepository;

    @GET
    @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "ALL OBJECTS", description = "list of all objects (only for admin)")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAll(){
        List<ObjetEntity> objetEntities = objetRepository.listAll();
        return Response.ok(ObjetDto.toDtoList(objetEntities)).build();
    }

    @GET
    @Path("{idObject}")
    @Operation(summary = "OBJECTS BY ID", description = "(only for admin)")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getObjectById(@PathParam("idObject") Integer idObject){
         ObjetEntity objetEntity = objetRepository.findById(idObject);
        return Response
                .status(200)
                .entity(objetEntity)
                .build();
    }



    @PUT
    @Path("{id}")
    //        @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "MODIFY BY ADMIN", description = "modify by admin (only for admin)")
    @Transactional
    public Response updateAdmin(@PathParam("id")Integer id,
                                @FormParam("1-nomObjet") String nomObjet,
                                @FormParam("2-Pays") String nomPays,
                                @FormParam("3-Type") String typeObjet){
        ObjetEntity objetEntity = objetRepository.findById(id);
        if (objetEntity == null)
            return Response.status(404)
                    .entity("Object not found")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        if (nomObjet == null || nomObjet.isEmpty())
            nomObjet = objetEntity.getLibelleObjet();
        if (nomPays == null || nomPays.isEmpty())
            nomPays = objetEntity.getPays().getLibellePays();
        else {
            if (paysRepository.findByName(nomPays) == null)
                paysRepository.persist(new PaysEntity(nomPays.toUpperCase()));
        }
        if (typeObjet == null || typeObjet.isEmpty())
            typeObjet = objetEntity.getTypeObjet().getNomTypeObjet();
        else {
            if (typeObjetRepository.findByName(typeObjet) == null)
                typeObjetRepository.persist(new TypeObjetEntity(typeObjet.toLowerCase()));
        }

        objetEntity.setLibelleObjet(nomObjet);
        objetEntity.setPays(paysRepository.findByName(nomPays));
        objetEntity.setTypeObjet(typeObjetRepository.findByName(typeObjet));
        objetRepository.persist(objetEntity);
        return Response.ok(objetEntity).build();
    }

    @DELETE
    @Path("{id}")
    //        @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "DELETE BY ADMIN", description = "delete by admin (for admin only)")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)

    public Response deleteAdmin(@PathParam("id") Integer id){
        ObjetEntity objetEntity = objetRepository.findById(id);
        if (objetEntity == null)
            return Response.status(404)
                    .entity("Object not found")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        objetRepository.deleteById(id);
        return Response.ok()
                .entity("Successfully delete the object id = " + id)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }


    @GET
    @Path("{login}")
//        @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "OBJECT BY LOGIN", description = "search object by user's login ")
    @APIResponse(responseCode = "200", description = "OK, object found")
    @APIResponse(responseCode = "404", description = "object not found")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response getByLoginUser(@PathParam("login") String login){
        if (userRepository.findById(login)==null)
            return Response.status(404)
                    .entity("Login is not existed")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        List<ObjetEntity> objetEntityList = objetRepository.findByLogin(login);
        if (objetEntityList.isEmpty())
            return Response.status(404)
                    .entity("No object is created by this login")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        return Response
                .status(200)
                .entity(objetEntityList)
                .build();
    }

    @GET
    @Path("{login}/{idType}")
//        @RolesAllowed({"admin", "superadmin"})
    @Operation(summary = "OBJECT BY LOGIN AND TYPE", description = "search object by user's login and type ")
    @APIResponse(responseCode = "200", description = "OK, object found")
    @APIResponse(responseCode = "404", description = "object not found")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response getByUserAndType(@PathParam("login") String login, @PathParam("idType") Integer idType ){
        if (userRepository.findById(login)==null)
            return Response.status(404)
                    .entity("Login is not existed")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        List<ObjetEntity> objetEntityList = objetRepository.findByLoginAndType(login, idType);
        if (objetEntityList.isEmpty())
            return Response.status(404)
                    .entity("No object is created by this login")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        return Response
                .status(200)
                .entity(objetEntityList)
                .build();
    }



    @POST
    @Path("{login}")
//        @RolesAllowed({"admin", "superadmin"},{user})
    @Operation(summary = "NEW", description = "Create a new object")
    @Transactional
    public Response create(@PathParam("login") String login,
                           @FormParam("1-nomObjet") String libelleObjet,
                           @FormParam("2-Pays") String nomPays,
                           @FormParam("3-Type") String nomTypeObjet){
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        if (userRepository.findById(login)==null)
            return Response.status(404)
                    .entity("Login is not existed")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        if (libelleObjet == null || libelleObjet.length() == 0)
            return Response.status(400)
                    .entity("Missing parameters")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        PaysEntity existingPays = paysRepository.findByName(nomPays);
        TypeObjetEntity existingType = typeObjetRepository.findByName(nomTypeObjet);

        if (existingPays == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("This country is not stored in the database.\nPlease check:\n - all available countries: " + uriBuilder.path("countries").build())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();

        if (existingType == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("This type is not stored in the database.\nPlease check:\n - all available types: " + uriBuilder.path("types").build())
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        ObjetEntity objetEntity = new ObjetEntity();
        objetEntity.setUser(userRepository.findById(login));
        objetEntity.setLibelleObjet(libelleObjet);
        objetEntity.setPays(existingPays);
        objetEntity.setTypeObjet(existingType);
        objetRepository.persist(objetEntity);
        return Response.ok(objetEntity).build();
    }

    @PUT
    @Path("{login}/{id}")
    //    @RolesAllowed({"admin", "superadmin"},{user})
    @Operation(summary = "MODIFY", description = "modify an object")
    @Transactional
    public Response update(@PathParam("login") String login,
                           @PathParam("id") Integer id,
                           @FormParam("1-nomObjet") String nomObjet,
                           @FormParam("2-Pays") String nomPays,
                           @FormParam("3-Type") String nomTypeObjet){
        ObjetEntity objetEntity = objetRepository.findById(id);
        if (objetEntity == null)
            return Response.status(404)
                    .entity("Object not found")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        if (!objetEntity.getUser().getLogin().equals(login))
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You are not the owner of this object")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        if (nomObjet == null || nomObjet.isEmpty())
            nomObjet = objetEntity.getLibelleObjet();
        if (nomPays == null || nomPays.isEmpty())
            nomPays = objetEntity.getPays().getLibellePays();
        else {
            if (paysRepository.findByName(nomPays) == null)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("This country is not stored in the database.\nPlease check:\n - all available countries: "+ uriInfo.getBaseUriBuilder().path("countries").build())
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .build();
        }
        if (nomTypeObjet == null || nomTypeObjet.isEmpty())
            nomTypeObjet = objetEntity.getTypeObjet().getNomTypeObjet();
        else {
            if (typeObjetRepository.findByName(nomTypeObjet) == null)
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("This type is not stored in the database.\nPlease check:\n - all available types: " + uriInfo.getBaseUriBuilder().path("types").build())
                        .type(MediaType.TEXT_PLAIN_TYPE)
                        .build();

        }

        objetEntity.setLibelleObjet(nomObjet);
        objetEntity.setPays(paysRepository.findByName(nomPays));
        objetEntity.setTypeObjet(typeObjetRepository.findByName(nomTypeObjet));
        objetRepository.persist(objetEntity);
        return Response.ok(objetEntity).build();
    }

    @DELETE
    @Path("{login}/{id}")
    //    @RolesAllowed({"admin", "superadmin"},{user})
    @Operation(summary = "DELETE", description = "delete an object")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)

    public Response delete(@PathParam("login") String login,
                           @PathParam("id") Integer id){
        ObjetEntity objetEntity = objetRepository.findById(id);
        if (objetEntity == null)
            return Response.status(404).entity("Object not found").type(MediaType.TEXT_PLAIN_TYPE).build();
        if (!objetEntity.getUser().getLogin().equals(login))
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You are not the owner of this object")
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        objetRepository.deleteById(id);
        return Response.ok()
                .entity("Successfully delete the object id = " + id)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

}
