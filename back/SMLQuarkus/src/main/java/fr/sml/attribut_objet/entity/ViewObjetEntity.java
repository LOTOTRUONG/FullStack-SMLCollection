package fr.sml.attribut_objet.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.io.Serializable;

@Entity
@NamedStoredProcedureQuery(name = "GetViewObjectData",
                        procedureName = "GetViewObjectData",
                        resultClasses = ViewObjetEntity.class)
@Getter
@Setter
@Table(name = "view_object")
@Immutable
@EqualsAndHashCode

public class ViewObjetEntity implements Serializable {
    @Id
    @Column(name = "login")
    private String login;
    @Id
    @Column(name = "id_objet")
    private Integer idObjet;
    @Column(name = "libelle_objet")
    private String libelleObjet;
    @Column(name = "id_pays")
    private int idPays;
    @Column(name = "libelle_pays")
    private String libellePays;
    @Column(name = "id_type")
    private int idType;
    @Column(name = "libelle_type")
    private String libelleType;
    @Id
    @Column(name = "id_attributType")
    private Integer idAttributType;
    @Column(name = "commentaire")
    private Boolean commentaire;
    @Column(name = "table_de_donnee")
    private Boolean tableDeDonnee;
    @Column(name = "valeurFloat")
    private Float valeurFloat;
    @Column(name = "valeurString")
    private String valeurString;
    @Column(name = "id_attribut")
    private Integer idAttribut;
    @Column(name = "libelle_attribut")
    private String libelleAttribut;
    @Column(name = "id_valeur")
    private Integer idValeur;
    @Column(name = "libelle_valeur")
    private String libelleValeur;
    @Column(name = "id_unite")
    private Integer idUnite;
    @Column(name = "libelle_unite")
    private String libelleUnite;

}
