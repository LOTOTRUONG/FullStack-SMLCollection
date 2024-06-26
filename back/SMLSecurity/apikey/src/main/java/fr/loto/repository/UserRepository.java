package fr.loto.repository;

import fr.loto.entities.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, String> {
    public UserEntity findByLogin(String login) {
        return find("login", login).firstResult();
    }

}
