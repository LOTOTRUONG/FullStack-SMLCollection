package fr.sml.objet.entity;

import fr.sml.pays.entity.PaysEntity;
import fr.sml.type_objet.entity.TypeObjetEntity;
import fr.sml.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "OBJET")
@Table(name = "objet")
@Getter
@Setter
@EqualsAndHashCode
public class ObjetEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login")
    private UserEntity user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objet")
    private Integer idObjet;
    @Basic
    @Column(name= "libelle_objet")
    private String libelleObjet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type")
    private TypeObjetEntity typeObjet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pays")
    private PaysEntity pays;





}
