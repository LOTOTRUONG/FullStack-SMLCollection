package fr.sml.valeur.repository;

import fr.sml.valeur.entity.ValeurEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped

public class ValeurRepository implements PanacheRepositoryBase<ValeurEntity, Integer> {
    public ValeurEntity findByName(String name){
        return find("libelleValeur", name).firstResult();
    }
}
