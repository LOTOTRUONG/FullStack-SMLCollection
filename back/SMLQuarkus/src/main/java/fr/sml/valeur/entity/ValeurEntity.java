package fr.sml.valeur.entity;

import fr.sml.attribut_type.entity.AttributTypeEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "valeur")
@Getter
@Setter
@EqualsAndHashCode

public class ValeurEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_valeur")
    private Integer idValeur;
    @Basic
    @Column(name = "libelle_valeur")
    private String libelleValeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "id_type", referencedColumnName = "id_type"),
            @JoinColumn(name = "id_attributType", referencedColumnName = "id_attributType")
    })
    private AttributTypeEntity attributType;

}
