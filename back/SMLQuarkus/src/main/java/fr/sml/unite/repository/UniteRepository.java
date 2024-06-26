package fr.sml.unite.repository;

import fr.sml.unite.entity.UniteEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class UniteRepository implements PanacheRepositoryBase<UniteEntity, Integer> {
    public UniteEntity findByName(String name){
        return find("libelleUnite", name).firstResult();
    }
}
