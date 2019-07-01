package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.FrontEndCorte;
import gaf.entity.Operator;
import gaf.entity.Talle;
import gaf.service.AttachService;
import gaf.service.CorteService;
import gaf.service.EstadoService;
import gaf.service.TalleService;
import gaf.util.OperatorRolManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.text.SimpleDateFormat;
import java.util.*;

@ViewScoped
@ManagedBean(name = "cortesController")
public class CommonCortesController {

    @EJB private AttachService attachService;
    @EJB private CorteService corteService;
    @EJB private EstadoService estadoService;
    @EJB private OperatorRolManager operatorRolManager;
    @EJB private TalleService talleService;

    private Operator operator;
    private List<FrontEndCorte> cortesNotFinished;
    private List<FrontEndCorte> cortesWithTallesInTaller;

    /**
     * Valor por el cual se realizara la busqueda de cortes
     */
    private String searchValue;

    @PostConstruct
    public void init() {
        operator = operatorRolManager.getOperatorFromSession(operator);
    }

    public List<FrontEndCorte> getCortesNotFinished() {
        if (cortesNotFinished == null) {
            cortesNotFinished = new ArrayList<>();
            List<Corte> cortes = corteService.findAllNotFinished();
            for (Corte c : cortes) {
                cortesNotFinished.add(new FrontEndCorte(c, generateCorteLabelForPanel(c)));
            }
        }
        return cortesNotFinished;
    }

    public void setCortesNotFinished(List<FrontEndCorte> cortesNotFinished) {
        this.cortesNotFinished = cortesNotFinished;
    }

    public List<FrontEndCorte> getCortesWithTallesInTaller(Integer tallerId) {
        if (cortesWithTallesInTaller == null) {
            cortesWithTallesInTaller = new ArrayList<>();
            if (tallerId != null) {
                List<Talle> talles = talleService.findTallesInTaller(tallerId);
                if (CollectionUtils.isNotEmpty(talles)) {
                    Map<Integer, FrontEndCorte> idFrontEndCortes = new TreeMap<>(Comparator.reverseOrder());
                    for (Talle talle : talles) {
                        Corte c = corteService.findById(talle.getCorteId());
                        if (!idFrontEndCortes.containsKey(c.getId())) {
                            FrontEndCorte corte = new FrontEndCorte(c, generateCorteLabelForPanel(c));
                            idFrontEndCortes.put(c.getId(), corte);
                        }
                    }

                    for (Integer corteId : idFrontEndCortes.keySet()) cortesWithTallesInTaller.add(idFrontEndCortes.get(corteId));
                }
            }
        }
        return cortesWithTallesInTaller;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public void search() {
        if (StringUtils.isNotEmpty(searchValue)) {
            cortesNotFinished = null;
            List<FrontEndCorte> filteredCortes = new ArrayList<>();
            for (FrontEndCorte corte : getCortesNotFinished()) {
                if (StringUtils.containsIgnoreCase(corte.getFrontEndLabel(), searchValue)) {
                    filteredCortes.add(corte);
                }
            }
            cortesNotFinished = filteredCortes;
        } else {
            cortesNotFinished = null;
        }
    }

    public List<Talle> getTallesOfCorte(Integer id) {
        return talleService.findTallesFromCorte(id);
    }

    public String getColorForCorte(Corte corte) {
        return estadoService.findById(corte.getEstadoId()).getColor();
    }

    public boolean haveAttachments(Integer corteId) {
        return CollectionUtils.isNotEmpty(attachService.findByCorteId(corteId));
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
