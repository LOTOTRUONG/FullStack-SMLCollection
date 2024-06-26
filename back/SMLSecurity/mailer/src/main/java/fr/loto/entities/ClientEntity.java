package fr.loto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "client", schema = "dbo", catalog = "SML")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Integer id;
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
    private List<EmailEntity> emailEntityList;


}
