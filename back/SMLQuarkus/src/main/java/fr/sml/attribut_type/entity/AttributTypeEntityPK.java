package fr.sml.attribut_type.entity;

import fr.sml.type_objet.entity.TypeObjetEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Embeddable
@Getter
@Setter
public class AttributTypeEntityPK implements Serializable {
    @Column(name = "id_attributType")
    private Integer idAttributType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type")
    private TypeObjetEntity typeObjet;


}
