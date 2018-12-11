package gaf.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CORTES")
public class CortesPagos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "CORTE_ID")
    private Integer corteId;

    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;

    @NotNull
    @Column(name = "SETTLED_AMOUNT")
    private Double settledAmount;

    @NotNull
    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    public Integer getCorteId() {
        return corteId;
    }

    public void setCorteId(@NotNull Integer corteId) {
        this.corteId = corteId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @NotNull
    public Double getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(@NotNull Double settledAmount) {
        this.settledAmount = settledAmount;
    }

    @NotNull
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(@NotNull Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "CortesPagos ["
                + ((id != null) ? "id=" + id + ", " : "")
                + ((corteId != null) ? "corteId=" + corteId + ", " : "")
                + ((paymentDate != null) ? "paymentDate=" + paymentDate + ", " : "")
                + ((settledAmount != null) ? "settledAmount=" + settledAmount + ", " : "")
                + ((totalAmount != null) ? "totalAmount=" + totalAmount : "")
                + "]";
    }
}
