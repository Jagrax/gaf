package gaf.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "CORTES")
public class Corte implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    private Double price;

    @NotNull
    @Column(name = "clothes_quantity")
    private Integer clothesQuantity;

    @NotNull
    @Column(name = "creation_date")
    private Date creationDate;

    @NotNull
    @Column(name = "due_date")
    private Date dueDate;

    @NotNull
    @Column(name = "from_size")
    private Double fromSize;

    @NotNull
    @Column(name = "to_size")
    private Double toSize;

    @Column(name = "hoja_de_corte")
    private String hojaDeCorte;

    @NotNull
    @Column(name = "estado")
    private Integer estadoId;

    private String comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClothesQuantity() {
        return clothesQuantity;
    }

    public void setClothesQuantity(Integer clothesQuantity) {
        this.clothesQuantity = clothesQuantity;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getFromSize() {
        return fromSize;
    }

    public void setFromSize(Double fromSize) {
        this.fromSize = fromSize;
    }

    public Double getToSize() {
        return toSize;
    }

    public void setToSize(Double toSize) {
        this.toSize = toSize;
    }

    public String getHojaDeCorte() {
        return hojaDeCorte;
    }

    public void setHojaDeCorte(String hojaDeCorte) {
        this.hojaDeCorte = hojaDeCorte;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Corte{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", clothesQuantity=" + clothesQuantity +
                ", creationDate=" + creationDate +
                ", dueDate=" + dueDate +
                ", fromSize=" + fromSize +
                ", toSize=" + toSize +
                ", hojaDeCorte='" + hojaDeCorte + '\'' +
                ", estadoId=" + estadoId +
                ", comments='" + comments + '\'' +
                '}';
    }
}
