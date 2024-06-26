package fr.sml.pays.repository;

import fr.sml.pays.entity.PaysEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class PaysRepository implements PanacheRepositoryBase<PaysEntity, Integer> {
    public PaysEntity findByName(String name){
        return find("libellePays", name).firstResult();
    }
}
