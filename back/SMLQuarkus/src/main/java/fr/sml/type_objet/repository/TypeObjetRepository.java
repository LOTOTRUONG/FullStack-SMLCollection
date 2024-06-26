package fr.sml.type_objet.repository;
import fr.sml.type_objet.entity.TypeObjetEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class TypeObjetRepository implements PanacheRepositoryBase<TypeObjetEntity, Integer> {
    public TypeObjetEntity findByName(String name){
        return find("nomTypeObjet", name).firstResult();
    }

}
