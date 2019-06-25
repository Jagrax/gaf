package gaf.controller;

import gaf.entity.*;
import gaf.service.CorteService;
import gaf.service.TalleService;
import gaf.service.TallerService;
import gaf.util.OperatorRolManager;
import org.apache.commons.collections4.CollectionUtils;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "tallerForm")
public class TallerFormController {

    private Operator operator;
    private Integer id;
    private Taller taller;
    private List<FrontEndCorte> cortes;

    @EJB private CorteService corteService;
    @EJB private TalleService talleService;
    @EJB private TallerService tallerService;

    @Inject private OperatorRolManager operatorRolManager;

    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (id != null) {
            taller = tallerService.findById(id);
        } else {
            taller = new Taller();
        }

        operator = operatorRolManager.getOperatorFromSession(operator);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Taller getTaller() {
        return taller;
    }

    public void setTaller(Taller taller) {
        this.taller = taller;
    }

    public List<FrontEndCorte> getCortes() {
        if (cortes == null) {
            cortes = new ArrayList<>();
            List<Talle> talles = talleService.findTallesInTaller(id);
            if (CollectionUtils.isNotEmpty(talles)) {
                Map<Integer, FrontEndCorte> idFrontEndCortes = new HashMap<>();
                for (Talle talle : talles) {
                    Corte c = corteService.findById(talle.getCorteId());
                    if (!idFrontEndCortes.containsKey(c.getId())) {
                        FrontEndCorte corte = new FrontEndCorte(c, generateCorteLabelForPanel(c));
                        idFrontEndCortes.put(c.getId(), corte);
                    }
                }

                for (Integer corteId : idFrontEndCortes.keySet()) cortes.add(idFrontEndCortes.get(corteId));
            }
        }
        return cortes;
    }

    public void setCortes(List<FrontEndCorte> cortes) {
        this.cortes = cortes;
    }

    public void remove() throws IOException {
        if (taller != null && taller.getId() != null) {
            tallerService.delete(taller);
            addDetailMessage("taller.delete", null, taller.getName());
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("tallerList.xhtml");
        }
    }

    public void save() throws IOException {
        String msg;
        if (taller.getId() == null) {
            tallerService.create(taller);
            msg = "taller.create";
        } else {
            tallerService.update(taller);
            msg = "taller.update";
        }
        addDetailMessage(msg, null, taller.getName());
        Faces.getFlash().setKeepMessages(true);
        Faces.redirect("tallerList.xhtml");
    }

    public void clear() {
        taller = new Taller();
        id = null;
    }

    public boolean isNew() {
        return taller == null || taller.getId() == null;
    }

    private String generateCorteLabelForPanel(Corte corte) {
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
