package fr.sml.attribut_type.entity;

import fr.sml.attribut.entity.AttributEntity;
import fr.sml.unite.entity.UniteEntity;
import fr.sml.type_objet.entity.TypeObjetEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attribut_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AttributTypeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_attributType")
    private Integer idAttributType;
    @Basic
    @Column(name = "commentaire")
    private boolean commentaire;
    @Basic
    @Column(name = "table_de_donnee")
    private boolean tableDeDonnee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type")
    private TypeObjetEntity typeObjet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_attribut")
    private AttributEntity attribut;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unite")
    private UniteEntity unite;
}
