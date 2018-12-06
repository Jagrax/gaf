package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.FrontEndCorte;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "corteList")
public class CorteListController extends AccessController {

    private List<FrontEndCorte> cortes;
    private String corteName;

    @EJB private AttachService attachService;
    @EJB private CorteService corteService;
    @EJB private TalleService talleService;
    @EJB private EstadoService estadoService;

    @PostConstruct
    public void init() {
    }

    public String getCorteName() {
        return corteName;
    }

    public void setCorteName(String corteName) {
        this.corteName = corteName;
    }

    public List<FrontEndCorte> getCortes() {
        if (cortes == null) {
            cortes = new ArrayList<>();
            List<Corte> cortesNotFinished = corteService.findAllNotFinished();
            for (Corte c : cortesNotFinished) {
                cortes.add(new FrontEndCorte(c, generateCorteLabelForPanel(c)));
            }
        }
        return cortes;
    }

    public void setCortes(List<FrontEndCorte> cortes) {
        this.cortes = cortes;
    }

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
        if (hasAccess("ADMIN")) {
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
