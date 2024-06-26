package fr.loto.repository;

import fr.loto.entities.ClientEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ClientRepository implements PanacheRepositoryBase<ClientEntity, Integer> {
    public ClientEntity findByApiKey(String apiKey) {
        return find("apiKey", apiKey).firstResult();
    }

}
