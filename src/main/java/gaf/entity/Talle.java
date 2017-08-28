package gaf.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TALLES")
public class Talle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer size;

    @NotNull
    private Integer quantity;

    @NotNull
    @Column(name = "clothes_delivered")
    private Integer clothesDelivered;

    @NotNull
    @Column(name = "first_due_date")
    private Date firstDueDate;

    @NotNull
    @Column(name = "second_due_date")
    private Date secondDueDate;

    private String comments;

    @NotNull
    @Column(name = "estado")
    private Integer estadoId;

    @Column(name = "taller")
    private Integer tallerId;

    @NotNull
    @Column(name = "corte")
    private Integer corteId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getClothesDelivered() {
        return clothesDelivered;
    }

    public void setClothesDelivered(Integer clothesDelivered) {
        this.clothesDelivered = clothesDelivered;
    }

    public Date getFirstDueDate() {
        return firstDueDate;
    }

    public void setFirstDueDate(Date firstDueDate) {
        this.firstDueDate = firstDueDate;
    }

    public Date getSecondDueDate() {
        return secondDueDate;
    }

    public void setSecondDueDate(Date secondDueDate) {
        this.secondDueDate = secondDueDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }

    public Integer getTallerId() {
        return tallerId;
    }

    public void setTallerId(Integer tallerId) {
        this.tallerId = tallerId;
    }

    public Integer getCorteId() {
        return corteId;
    }

    public void setCorteId(Integer corteId) {
        this.corteId = corteId;
    }

    @Override
    public String toString() {
        return "Talle{" +
                "id=" + id +
                ", size=" + size +
                ", quantity=" + quantity +
                ", clothesDelivered=" + clothesDelivered +
                ", firstDueDate=" + firstDueDate +
                ", secondDueDate=" + secondDueDate +
                ", comments='" + comments + '\'' +
                ", estadoId=" + estadoId +
                ", tallerId=" + tallerId +
                ", corteId=" + corteId +
                '}';
    }
}
