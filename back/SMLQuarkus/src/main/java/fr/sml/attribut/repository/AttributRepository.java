package fr.sml.attribut.repository;

import fr.sml.attribut.entity.AttributEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class AttributRepository implements PanacheRepositoryBase<AttributEntity, Integer> {
    public AttributEntity findByName(String name){
        return find("libelleAttribut", name).firstResult();
    }
}
