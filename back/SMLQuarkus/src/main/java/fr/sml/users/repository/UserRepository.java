package fr.sml.users.repository;
import fr.sml.users.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;


@RequestScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, String> {
    public UserEntity findByEmail(String email) {
        return find("email", email).firstResult();
    }


}
