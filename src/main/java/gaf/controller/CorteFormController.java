package gaf.controller;

import gaf.entity.Corte;
import gaf.service.CorteService;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "corteForm")
public class CorteFormController {

    private Integer id;
    private Corte corte;

    @EJB
    private CorteService corteService;

    @PostConstruct
    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (id != null) {
            corte = corteService.findById(id);
        } else {
            corte = new Corte();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Corte getCorte() {
        return corte;
    }

    public void setCorte(Corte corte) {
        this.corte = corte;
    }

    public void remove() throws IOException {
        if (corte != null && corte.getId() != null) {
            corteService.delete(corte);
            addDetailMessage("El corte " + corte.getName() + " se eliminó correctamente");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("corteList.xhtml");
        }
    }

    public void save() throws IOException {
        String msg;
        if (corte.getId() == null) {
            corteService.create(corte);
            msg = "El corte " + corte.getName() + " se creó correctamente";
        } else {
            corteService.update(corte);
            msg = "El corte " + corte.getName() + " se actualizó correctamente";
        }
        addDetailMessage(msg);
        Faces.getFlash().setKeepMessages(true);
        Faces.redirect("corteList.xhtml");
    }

    public void clear() {
        corte = new Corte();
        id = null;
    }

    public boolean isNew() {
        return corte == null || corte.getId() == null;
    }
}
