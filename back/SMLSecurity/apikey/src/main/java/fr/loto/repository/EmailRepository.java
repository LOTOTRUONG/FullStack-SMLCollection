package fr.loto.repository;

import fr.loto.entities.ClientEntity;
import fr.loto.entities.EmailEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class EmailRepository implements PanacheRepositoryBase<EmailEntity, Integer> {
    @Inject
    EntityManager entityManager;
    @Transactional
    public Long countEmailByApiKey(String apiKey) {
        return entityManager.createQuery(
                        "SELECT COUNT(e) FROM EmailEntity e " +
                                "JOIN e.client c " +
                                "WHERE c.apiKey = :apiKey " +
                                "AND MONTH(e.sendAt) = MONTH(CURRENT_TIMESTAMP)" +
                                "AND YEAR(e.sendAt) = YEAR(CURRENT_TIMESTAMP)", Long.class)
                .setParameter("apiKey", apiKey)
                .getSingleResult();

    }

}
