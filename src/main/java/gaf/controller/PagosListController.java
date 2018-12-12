package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.CortesPagos;
import gaf.entity.FrontEndCorte;
import gaf.service.CorteService;
import gaf.service.CortesPagosService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "pagosList")
public class PagosListController {

    @Inject private CorteService corteService;
    @Inject private CortesPagosService cortesPagosService;

    private boolean authorized;
    private String pagePassword;
    private List<FrontEndCorte> cortes;
    private String corteName;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String authorized = params.get("authorized");
        if (StringUtils.isNotEmpty(authorized)) {
            this.authorized = true;
        }
    }

    public List<FrontEndCorte> getCortes() {
        if (cortes == null) {
            cortes = new ArrayList<>();
            List<Corte> cortesNotFinished = corteService.findAll();
            for (Corte c : cortesNotFinished) {
                cortes.add(new FrontEndCorte(c, generateCorteLabelForPanel(c)));
            }
        }
        return cortes;
    }

    public void setCortes(List<FrontEndCorte> cortes) {
        this.cortes = cortes;
    }

    public String getCorteName() {
        return corteName;
    }

    public void setCorteName(String corteName) {
        this.corteName = corteName;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public String getPagePassword() {
        return pagePassword;
    }

    public void setPagePassword(String pagePassword) {
        this.pagePassword = pagePassword;
    }

    // METHODS
    public void search() {
        if (StringUtils.isNotEmpty(corteName)) {
            List<FrontEndCorte> filteredCortes = new ArrayList<>();
            for (FrontEndCorte corte : cortes) {
                if (corte.getFrontEndLabel().contains(corteName)) {
                    filteredCortes.add(corte);
                }
            }
            cortes = filteredCortes;
        } else {
            cortes = null;
        }
    }

    public String generateCorteLabelForPanel(Corte corte) {
        String label = corte.getName();
        if (corte.getPrice() != null) label += "|$" + corte.getPrice();
        label += "|Cant. de prendas: ";
        Integer clothesQuantity = corte.getClothesQuantity();
        if (clothesQuantity == null) {
            label += "No asignadas";
        } else {
            label += clothesQuantity;
        }
        Date creationDate = corte.getCreationDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        label += "|F. de Alta: " + sdf.format(creationDate);

        return label;
    }

    public CortesPagos getPagosByCorteId(Integer id) {
        return cortesPagosService.findByCorteId(id);
    }

    public String getStatusMessage(Integer id) {
        CortesPagos cortesPagos = cortesPagosService.findByCorteId(id);
        if (cortesPagos == null || cortesPagos.getSettledAmount() == null || cortesPagos.getSettledAmount() == 0) {
            return "Sin pagar";
        } else if (cortesPagos.getSettledAmount() < cortesPagos.getTotalAmount()) {
            return "Pago parcial";
        } else if (cortesPagos.getSettledAmount().equals(cortesPagos.getTotalAmount())) {
            return "Todo pago";
        } else {
            return "Los montos ingresados estan raros";
        }
    }

    public void authorize() {
        authorized = pagePassword.equals("966393");
    }
}
