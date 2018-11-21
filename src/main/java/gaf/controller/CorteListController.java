package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.Talle;
import gaf.service.AttachService;
import gaf.service.CorteService;
import gaf.service.EstadoService;
import gaf.service.TalleService;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ViewScoped
@ManagedBean(name = "corteList")
public class CorteListController {

    private List<Corte> cortes;

    @EJB private AttachService attachService;
    @EJB private CorteService corteService;
    @EJB private TalleService talleService;
    @EJB private EstadoService estadoService;

    @PostConstruct
    public void init() {
        cortes = corteService.findAll();
    }

    public List<Corte> getCortes() {
        return cortes;
    }

    public void setCortes(List<Corte> cortes) {
        this.cortes = cortes;
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
}
