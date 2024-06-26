package fr.sml.pays.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "pays")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PaysEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pays")
    private Integer idPays;

    @Column(name = "libelle_pays")
    private String libellePays;

    public PaysEntity(String libellePays){
        this.libellePays = libellePays;
    }
}
