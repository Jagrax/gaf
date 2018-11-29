package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.Talle;
import gaf.service.AttachService;
import gaf.service.CorteService;
import gaf.service.EstadoService;
import gaf.service.TalleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "corteList")
public class CorteListController {

    private List<Corte> cortes;
    private String corteName;

    @EJB private AttachService attachService;
    @EJB private CorteService corteService;
    @EJB private TalleService talleService;
    @EJB private EstadoService estadoService;

    @PostConstruct
    public void init() {
        cortes = corteService.findAllNotFinished();
    }

    public String getCorteName() {
        return corteName;
    }

    public void setCorteName(String corteName) {
        this.corteName = corteName;
    }

    public List<Corte> getCortes() {
        return cortes;
    }

    public void setCortes(List<Corte> cortes) {
        this.cortes = cortes;
    }

    public void search() {
        if (StringUtils.isNotEmpty(corteName)) {
            List<Corte> filteredCortes = new ArrayList<>();
            for (Corte corte : cortes) {
                if (generateCorteLabelForPanel(corte).contains(corteName)) {
                    filteredCortes.add(corte);
                }
            }
            cortes = filteredCortes;
        } else {
            cortes = corteService.findAllNotFinished();
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

        return label;
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

    public class FrontEndCorte extends Corte {
        private String frontEndLabel;

    }
}
