package fr.sml.attribut_objet.repository;

import fr.sml.attribut_objet.entity.AttributObjetEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class AttributObjetRepository implements PanacheRepositoryBase<AttributObjetEntity, Integer> {

    public AttributObjetEntity findByIdAttributType(Integer id) {
        return find("attributObjetEntityPK.idAttributType", id).firstResult();
    }

    public AttributObjetEntity findByIdType(Integer id) {
        return find("attributObjetEntityPK.idType", id).firstResult();
    }


}
