package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.FrontEndCorte;
import gaf.entity.Operator;
import gaf.service.CorteService;
import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "pagosList")
public class PagosListController {

    @Inject private CorteService corteService;

    private List<FrontEndCorte> cortes;
    private String corteName;

    public List<FrontEndCorte> getCortes() {
        if (cortes == null) {
            cortes = new ArrayList<>();
            List<Corte> cortesNotFinished = corteService.findAll();
            for (Corte c : cortesNotFinished) {
//                cortes.add(new FrontEndCorte(c, generateCorteLabelForPanel(c)));
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

    // METHODS
    /*
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
        if (operatorRolManager.hasAccess(operator.getRol(), Operator.Rol.ADMINISTRATOR)) {
            if (corte.getPrice() != null) label += "|$" + corte.getPrice();
        }
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
    }*/
}
