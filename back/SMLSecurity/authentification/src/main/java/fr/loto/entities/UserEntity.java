package fr.loto.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @Basic
    @Column(name = "login", unique=true)
    private String login;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "email", unique = true)
    private String email;
    @Basic
    @Column(name = "role")
    private String roleUser;
    @Basic
    @Column(name = "date_connexion")
    private LocalDateTime connectionDate;
    @Basic
    @Column(name = "code_pin")
    private Integer codePin;
    @Basic
    @Column(name = "accessFail")
    private Integer loginAttempts = 0;

    @Column(name = "status")
    private Boolean status = true;

}
