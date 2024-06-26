package fr.sml.attribut_objet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class AttributObjetEntityPK implements Serializable {
    @Column(name = "id_attributType")
    private Integer idAttributType;

    @Column(name = "id_type")
    private Integer idType;

    @Column(name = "id_objet")
    private Integer idObjet;

}
