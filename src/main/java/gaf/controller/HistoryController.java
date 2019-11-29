package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.FrontEndCorte;
import gaf.entity.Operator;
import gaf.service.CorteService;
import gaf.util.Estados;
import gaf.util.OperatorRolManager;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.text.SimpleDateFormat;
import java.util.*;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "historyController")
public class HistoryController {

    @EJB private CorteService corteService;
    @EJB private OperatorRolManager operatorRolManager;

    private Operator operator;
    private List<FrontEndCorte> cortesFinalizados;

    /**
     * Valor por el cual se realizara la busqueda de cortes
     */
    private String searchValue;

    @PostConstruct
    public void init() {
        operator = operatorRolManager.getOperatorFromSession(operator);
    }

    public List<FrontEndCorte> getCortesFinalizados() {
        if (cortesFinalizados == null) {
            cortesFinalizados = new ArrayList<>();
            List<Corte> cortes = corteService.findByStatus(Collections.singletonList(Estados.CORTE_FINALIZADO.getId()));
            for (Corte c : cortes) {
                cortesFinalizados.add(new FrontEndCorte(c, generateCorteLabelForPanel(c)));
            }
        }
        return cortesFinalizados;
    }

    public void setCortesFinalizados(List<FrontEndCorte> cortesFinalizados) {
        this.cortesFinalizados = cortesFinalizados;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public void markCorteAsInProcess(Corte corte) {
        corte.setEstadoId(Estados.CORTE_EN_PRODUCCION.getId());
        corteService.update(corte);
        addDetailMessage("corte.update.historyToCorte", null, corte.getName());
        cortesFinalizados = null;
    }

    public void search() {
        if (StringUtils.isNotEmpty(searchValue)) {
            cortesFinalizados = null;
            List<FrontEndCorte> filteredCortes = new ArrayList<>();
            for (FrontEndCorte corte : getCortesFinalizados()) {
                if (StringUtils.containsIgnoreCase(corte.getFrontEndLabel(), searchValue)) {
                    filteredCortes.add(corte);
                }
            }
            cortesFinalizados = filteredCortes;
        } else {
            cortesFinalizados = null;
        }
    }

    public String generateCorteLabelForPanel(Corte corte) {
        String label = corte.getName();
        if (operatorRolManager.hasAccess(operator.getRol(), Operator.Rol.ADMINISTRATOR)) {
            if (corte.getPrice() != null) label += " ($" + corte.getPrice() + ")";
        }
        label += "\nCant. de prendas: ";
        Integer clothesQuantity = corte.getClothesQuantity();
        if (clothesQuantity == null) {
            label += "No asignadas";
        } else {
            label += clothesQuantity;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date creationDate = corte.getCreationDate();
        label += "\nF. de Alta: " + (creationDate != null ? sdf.format(creationDate) : "no establecida");

        Date dueDate = corte.getDueDate();
        label += " - F. de Vencimiento: " + (dueDate != null ? sdf.format(dueDate) : "no establecida");

        return label;
    }
}
