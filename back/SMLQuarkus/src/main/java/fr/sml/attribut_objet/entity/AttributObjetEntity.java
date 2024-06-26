package fr.sml.attribut_objet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NamedStoredProcedureQuery(
        name = "InsertNewAttributeObject",
        procedureName = "InsertNewAttributeObject",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "nomType", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "login", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "nomObjet", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "valeurFloat", type = Float.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "valeurString", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "idAttributType", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "nomValeur", type = String.class),

        }
)
@Table(name = "attribut_objet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributObjetEntity {
    @EmbeddedId
    AttributObjetEntityPK attributObjetEntityPK;
    @Column(name = "valeurFloat")
    private Float valeurFloat;
    @Column(name = "valeurString")
    private String valeurString;
    @Column(name = "id_valeur")
    private Integer idValeur;
    @Column(name = "id_type_valeur")
    private Integer idTypeValeur;
    @Column(name = "id_attributType_valeur")
    private Integer idAttributTypeValeur;
}
