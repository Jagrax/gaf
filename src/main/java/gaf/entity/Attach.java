package gaf.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "ATTACHS")
public class Attach implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String path;

    @NotNull
    @Column(name = "file_name")
    private String filename;

    @NotNull
    @Column(name = "CORTE_ID")
    private Integer corteId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    public String getPath() {
        return path;
    }

    public void setPath(@NotNull String path) {
        this.path = path;
    }

    @NotNull
    public String getFilename() {
        return filename;
    }

    public void setFilename(@NotNull String filename) {
        this.filename = filename;
    }

    @NotNull
    public Integer getCorteId() {
        return corteId;
    }

    public void setCorteId(@NotNull Integer corteId) {
        this.corteId = corteId;
    }

    @Override
    public String toString() {
        return "Attach ["
                + ((id != null) ? "id=" + id + ", " : "")
                + ((path != null) ? "path=" + path + ", " : "")
                + ((filename != null) ? "filename=" + filename + ", " : "")
                + ((corteId != null) ? "corteId=" + corteId : "")
                + "]";
    }
}
