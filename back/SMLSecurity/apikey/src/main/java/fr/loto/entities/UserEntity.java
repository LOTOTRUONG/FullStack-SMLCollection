package fr.loto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String role;
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




    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        UserEntity user = (UserEntity) object;

        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
