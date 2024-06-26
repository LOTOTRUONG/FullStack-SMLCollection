package fr.sml.objet.repository;

import fr.sml.objet.entity.ObjetEntity;
import fr.sml.type_objet.entity.TypeObjetEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class ObjetRepository implements PanacheRepositoryBase<ObjetEntity, Integer> {
    public ObjetEntity findByName(String name) {
        return find("libelleObjet", name).firstResult();
    }

    public List<ObjetEntity> findByLogin(String login){
        return find("user.login", login).list();
    }

    public List<ObjetEntity> findByLoginAndType(String login, Integer idTypeObject){
        return find("user.login = ?1 and typeObjet.idType = ?2", login, idTypeObject).list();
    }
}
