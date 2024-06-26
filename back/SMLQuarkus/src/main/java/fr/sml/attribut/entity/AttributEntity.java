package fr.sml.attribut.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attribut")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AttributEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_attribut")
    private Integer idAttribut;
    @Basic
    @Column(name = "libelle_attribut")
    private String libelleAttribut;

}
