package fr.sml.unite.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UniteEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_unite")
    private Integer idUnite;
    @Basic
    @Column(name = "libelle_unite")
    private String libelleUnite;

}
