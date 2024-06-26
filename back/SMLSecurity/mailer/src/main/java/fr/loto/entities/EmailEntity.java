package fr.loto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Table(name = "log_message", schema = "dbo", catalog = "SML")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private Integer idMessage;
    @Basic
    @Column(name = "subject")
    private String subject;
    @Basic
    @Column(name = "date_envoi")
    private LocalDateTime sendAt;
    @Basic
    @Column(name = "destinataire")
    private String sendTo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private ClientEntity client;

}
