package fr.sml.attribut_type.repository;

import fr.sml.attribut_type.entity.AttributTypeEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class AttributTypeRepository implements PanacheRepositoryBase<AttributTypeEntity, Integer> {

    public List<AttributTypeEntity> findByIdType(Integer idType) {
        return find("typeObjet.idType", idType).list();
    }
    public AttributTypeEntity findByIdAttributType(Integer idAttributType) {
        return find("idAttributType", idAttributType).firstResult();
    }
}
