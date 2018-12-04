package gaf.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "ACCESS")
public class Access implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "OPERADOR_ID")
    private Integer operadorId;

    @NotNull
    @Column(name = "ROL_ID")
    private Integer rolId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    public Integer getOperadorId() {
        return operadorId;
    }

    public void setOperadorId(@NotNull Integer operadorId) {
        this.operadorId = operadorId;
    }

    @NotNull
    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(@NotNull Integer rolId) {
        this.rolId = rolId;
    }
}
