package gaf.controller;

import gaf.entity.Talle;
import gaf.entity.Taller;
import gaf.service.TalleService;
import gaf.service.TallerService;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "tallerList")
public class TallerListController {

    private String tallerName;
    private List<Taller> talleresSeleccionados;
    private List<Taller> talleres;
    private List<Talle> tallesInTaller;
    @EJB private TallerService tallerService;
    @EJB private TalleService talleService;
    private Taller dialogTaller;

    @PostConstruct
    public void init() {

    }

    public String getTallerName() {
        return tallerName;
    }

    public void setTallerName(String tallerName) {
        this.tallerName = tallerName;
    }

    public List<Taller> getTalleresSeleccionados() {
        return talleresSeleccionados;
    }

    public void setTalleresSeleccionados(List<Taller> talleresSeleccionados) {
        this.talleresSeleccionados = talleresSeleccionados;
    }

    public List<Taller> getTalleres() {
        if (talleres == null) {
            talleres = tallerService.findAll();
        }
        return talleres;
    }

    public void setTalleres(List<Taller> talleres) {
        this.talleres = talleres;
    }

    public Taller getDialogTaller() {
        return dialogTaller;
    }

    public void setDialogTaller(Taller dialogTaller) {
        this.dialogTaller = dialogTaller;
        this.tallesInTaller = null;
    }

    public List<Talle> getTallesInTaller() {
        if (tallesInTaller == null && dialogTaller != null) {
            tallesInTaller = talleService.findTallesInTaller(dialogTaller.getId());
        }
        return tallesInTaller;
    }

    public void setTallesInTaller(List<Talle> tallesInTaller) {
        this.tallesInTaller = tallesInTaller;
    }

    public void delete() {
        int cantTalleres = 0;
        String lastTallerName = null;
        for (Taller tallerSeleccionado : talleresSeleccionados) {
            try {
                tallerService.delete(tallerSeleccionado);
                talleres.remove(tallerSeleccionado);
                cantTalleres++;
                lastTallerName = tallerSeleccionado.getName();
            } catch (Exception e) {
                addDetailMessage("taller.delete.error.foreignkey", FacesMessage.SEVERITY_WARN, tallerSeleccionado.getName());
            }
        }
        talleresSeleccionados.clear();
        if (cantTalleres > 1) {
            addDetailMessage("taller.delete.multiple", null, cantTalleres);
        } else if (cantTalleres == 1) {
            addDetailMessage("taller.delete", null, lastTallerName);
        }
    }
    
    public void search() {
        if (StringUtils.isNotEmpty(tallerName)) {
            talleres = null;
            List<Taller> filteredTalleres = new ArrayList<>();
            for (Taller taller : getTalleres()) {
                if (StringUtils.containsIgnoreCase(taller.getName(), tallerName)) {
                    filteredTalleres.add(taller);
                }
            }
            talleres = filteredTalleres;
        } else {
            talleres = null;
        }
    }

    public String getDialogTitle() {
        String dialogTallerName = "";
        if (dialogTaller != null) {
            dialogTallerName = dialogTaller.getName();
        }
        return "Cortes y talles asignados a " + dialogTallerName;
    }
}
