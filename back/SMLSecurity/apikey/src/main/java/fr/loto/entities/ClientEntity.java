package fr.loto.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Integer idClient;
    @Basic
    @Column(name = "apiKey")
    private String apiKey;
    @Basic
    @Column(name = "email")
    private String emailClient;

    @Basic
    @Column(name = "nom")
    private String nomClient;

    @Column(name = "monthly_quota")
    private Integer monthlyQuota;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<EmailEntity> emailEntities;


    public ClientEntity(Integer id, String apiKey, String emailClient, String nomClient, Integer monthlyQuota) {
        this.idClient = id;
        this.apiKey = apiKey;
        this.emailClient = emailClient;
        this.nomClient = nomClient;
        this.monthlyQuota = monthlyQuota;
    }

}
