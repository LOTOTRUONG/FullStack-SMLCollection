package fr.loto.repository;
import fr.loto.entities.EmailEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class EmailRepository implements PanacheRepositoryBase<EmailEntity, Integer> {

}
