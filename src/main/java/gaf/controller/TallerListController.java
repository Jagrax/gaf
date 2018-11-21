package gaf.controller;

import gaf.entity.Taller;
import gaf.service.TallerService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "tallerList")
public class TallerListController {

    private List<Taller> talleresSeleccionados;
    private List<Taller> talleres;
    @EJB private TallerService tallerService;

    @PostConstruct
    public void init() {
        talleres = tallerService.findAll();
    }

    public List<Taller> getTalleresSeleccionados() {
        return talleresSeleccionados;
    }

    public void setTalleresSeleccionados(List<Taller> talleresSeleccionados) {
        this.talleresSeleccionados = talleresSeleccionados;
    }

    public List<Taller> getTalleres() {
        return talleres;
    }

    public void setTalleres(List<Taller> talleres) {
        this.talleres = talleres;
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
}
