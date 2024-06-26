package fr.sml.type_objet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "type_objet")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TypeObjetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type")
    private Integer idType;
    @Basic
    @Column(name = "libelle_type")
    private String nomTypeObjet;

    public TypeObjetEntity(String nomTypeObjet) {
        this.nomTypeObjet = nomTypeObjet;
    }


}
