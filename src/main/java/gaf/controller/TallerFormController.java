package gaf.controller;

import gaf.entity.Taller;
import gaf.service.TallerService;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "tallerForm")
public class TallerFormController {

    private Integer id;
    private Taller taller;

    @EJB
    private TallerService tallerService;

    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (id != null) {
            taller = tallerService.findById(id);
        } else {
            taller = new Taller();
        }
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

    public void remove() throws IOException {
        if (taller != null && taller.getId() != null) {
            tallerService.delete(taller);
            addDetailMessage("El taller " + taller.getName() + " se eliminó correctamente");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("tallerList.xhtml");
        }
    }

    public void save() throws IOException {
        String msg;
        if (taller.getId() == null) {
            tallerService.create(taller);
            msg = "El taller " + taller.getName() + " se creó correctamente";
        } else {
            tallerService.update(taller);
            msg = "El taller " + taller.getName() + " se actualizó correctamente";
        }
        addDetailMessage(msg);
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
}
