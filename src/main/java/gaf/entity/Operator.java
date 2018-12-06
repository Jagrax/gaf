package gaf.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OPERATORS")
public class Operator {

    public enum Rol {
        SU,
        ADMINISTRATOR,
        WORKER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String username;

    private String password;

    private String salt;

    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Rol rol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    public Rol getRol() {
        return rol;
    }

    public void setRol(@NotNull Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Operator ["
                + ((id != null) ? "id=" + id + ", " : "")
                + ((username != null) ? "username=" + username + ", " : "")
                + ((description != null) ? "description=" + description + ", " : "")
                + ((rol != null) ? "rol=" + rol : "")
                + "]";
    }
}
