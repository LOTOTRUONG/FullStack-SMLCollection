package fr.sml.attribut_objet.repository;
import fr.sml.attribut_objet.entity.ViewObjetEntity;
import fr.sml.objet.entity.ObjetEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class ViewObjetRepository implements PanacheRepositoryBase <ViewObjetEntity, String> {

    public List<ViewObjetEntity> findByLogin(String login) {
        return find("login", login).list();
    }
    public List<ViewObjetEntity> findDetailObject(String login, Integer idType, Integer idObject){
        return find("login = ?1 and idType = ?2 and idObjet = ?3", login, idType, idObject).list();
    }
}
